package com.zhi.pin.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhi.pin.common.BaseResponse;
import com.zhi.pin.common.ErrorCode;
import com.zhi.pin.common.ResultUtils;
import com.zhi.pin.config.RedisConfig;
import com.zhi.pin.exception.ThrowUtils;
import com.zhi.pin.manager.AiManager;
import com.zhi.pin.mapper.InterviewFeedbackMapper;
import com.zhi.pin.mapper.InterviewRecordsMapper;
import com.zhi.pin.mapper.PositionsMapper;
import com.zhi.pin.model.dto.AiGenerateInterviewQuestionRequest;
import com.zhi.pin.model.dto.AiInterviewMessage;
import com.zhi.pin.model.dto.InterviewRequest;
import com.zhi.pin.model.dto.QuestionContentDTO;
import com.zhi.pin.model.entity.*;
import com.zhi.pin.model.enums.PositionTypeEnum;
import com.zhi.pin.service.InterviewRecordsService;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.Flowable;
import io.reactivex.rxjava3.subscribers.DefaultSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.zhi.pin.common.ErrorCode.OPERATION_ERROR;
import static com.zhi.pin.common.ErrorCode.SYSTEM_ERROR;


@RestController
@RequestMapping("/ai_interview")
public class InterviewRecordsController {

    @Resource
    private InterviewRecordsService interviewRecordsService;

    @Resource
    private AiManager aiManager;

    @Resource
    private PositionsMapper positionsMapper;

    @Resource
    private InterviewRecordsMapper interviewRecordsMapper;

    @Resource
    private InterviewFeedbackMapper interviewFeedbackMapper;

    @Autowired
    private RedisPublisher redisPublisher;

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private MessageQueueService messageQueueService;

    @Autowired
    private RedisMessageListenerContainer redisContainer;

    // 获取特定频道的下一条消息
    @GetMapping("/poll-messages")
    public String pollMessages(@RequestParam String channel) {
        String message = messageQueueService.getNextMessage(channel);
        return message != null ? message : "No new messages for channel: " + channel;
    }

    @GetMapping("/send-question")
    public String sendQuestion(@RequestParam String interviewId, @RequestParam String userId, @RequestParam String question) {
        String channel = "channel:interview:" + interviewId+ " " + userId;
        // 动态添加订阅
        redisConfig.addSubscription(channel, redisContainer);
        // 发布消息
        redisPublisher.publish(channel, question);
        return "Question sent to interview ID " + interviewId + ": " + question;
    }
    // 每隔5秒推送一次数据
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, String>> streamData() {
        return Flux.fromStream(Stream.generate(() -> {
                    String data = "SSE data at " + LocalTime.now();
                    return Map.of("message", data);
                }))
                .delayElements(Duration.ofSeconds(5)); // 每5秒推送一次
    }


    @PostMapping("/get")
    public BaseResponse<List<InterviewRecords>> getInterviewRecordList(@RequestBody InterviewRequest request) {
        QueryWrapper<InterviewRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interview_id", request.getInterviewId());
        List<InterviewRecords> list = interviewRecordsMapper.selectList(queryWrapper);
        return ResultUtils.success(list);
    }

    @PostMapping("/add")
    public BaseResponse<InterviewRecords> add(@RequestBody InterviewRecords interviewRecords) {
        boolean save = interviewRecordsService.save(interviewRecords);
        if (save){
            return ResultUtils.success(interviewRecords);
        }else{
            return ResultUtils.error(SYSTEM_ERROR);
        }
    }

    // region AI 生成面试题目功能
    private static final String GENERATE_INTERVIEW_QUESTION_SYSTEM_MESSAGE = "你是一位专业的面试题专家，我会给你如下信息：\n" +
            "```\n" +
            "岗位名称，\n" +
            "【【【岗位描述】】】，\n" +
            "岗位类别，\n" +
            "要生成的面试题目数，\n" +
            "每个题目的选项数\n" +
            "```\n" +
            "\n" +
            "请你根据上述信息，按照以下步骤来生成面试题目：\n" +
            "1. 要求：题目和选项尽可能地短，题目不要包含序号，每题的选项数以我提供的为主，题目不能重复\n" +
            "2. 严格按照下面的 json 格式输出题目和选项\n" +
            "```\n" +
            "[{\"title\":\"题目标题\"}]\n" +
            "```\n" +
            "title 是题目，options 是选项，每个选项的 key 按照英文字母序（比如 A、B、C、D）以此类推，value 是选项内容\n" +
            "3. 检查题目是否包含序号，若包含序号则去除序号\n" +
            "4. 返回的题目列表格式必须为 JSON 数组" +
            "5. 不需要返回给我其他的描述，一定要是JSON数组" +
            "6. 我需要7个技术问题，并且字数控制在140个字";

    /**
     * 生成面试题目接口
     *
     * @param aiGenerateInterviewQuestionRequest 请求参数，包含岗位信息和题目生成数目
     * @return 面试题目列表
     */
    @PostMapping("/ai_generate_interview")
    public BaseResponse<String> aiGenerateInterviewQuestion(
            @RequestBody AiGenerateInterviewQuestionRequest aiGenerateInterviewQuestionRequest) {
        ThrowUtils.throwIf(aiGenerateInterviewQuestionRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取参数
        Long positionId = aiGenerateInterviewQuestionRequest.getPositionId();
        int questionNumber = aiGenerateInterviewQuestionRequest.getQuestionNumber();
        int optionNumber = aiGenerateInterviewQuestionRequest.getOptionNumber();
        // 获取岗位信息
        Positions positionById = positionsMapper.selectById(aiGenerateInterviewQuestionRequest.getPositionId());
        ThrowUtils.throwIf(positionById == null, ErrorCode.NOT_FOUND_ERROR);
        // 封装 Prompt
        String userMessage = getGenerateInterviewQuestionUserMessage(positionById, questionNumber, optionNumber);
        // AI 生成
        String result = aiManager.generateStableInterviewQuestions(GENERATE_INTERVIEW_QUESTION_SYSTEM_MESSAGE, userMessage);
        // 截取需要的 JSON 信息
        int start = result.indexOf("[");
        int end = result.lastIndexOf("]");
        String json = result.substring(start, end + 1);
        json = json.replace("\\", "").replace("n{","{");
        System.out.println(json);
        InterviewRecords interviewRecords = new InterviewRecords(json,aiGenerateInterviewQuestionRequest.getInterviewId(),"waiting");
        QueryWrapper<InterviewRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interview_id", interviewRecords.getInterview_id()).eq("record_id", aiGenerateInterviewQuestionRequest.getRecordId());
        interviewRecordsMapper.update(interviewRecords, queryWrapper);
        return ResultUtils.success(json);
    }

    /**
     * 一对一 AI 面试对话接口,面试者的自我介绍
     *
     * @param aiInterviewMessage 候选人的回答信息
     * @return AI 面试官的回复
     */
    @PostMapping("/start_conversation")
    public BaseResponse<String> startInterviewConversation(@RequestBody AiInterviewMessage aiInterviewMessage) {
        // 使用稳定的对话方式启动一对一面试
        String result = aiManager.startStableInterview(aiInterviewMessage.getUserMessage());
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ApiResponse response = objectMapper.readValue(result, ApiResponse.class);
            System.out.println("Message Content: " + response.getMessage().getContent());
            SelfInterview selfInterview = objectMapper.readValue(response.getMessage().getContent(), SelfInterview.class);
            System.out.println("Self Interview"+selfInterview);
            InterviewRecords interviewRecords = new InterviewRecords();
            interviewRecords.setInterview_id(aiInterviewMessage.getInterview_id());
            interviewRecords.setCandidate_response(aiInterviewMessage.getUserMessage());
            interviewRecords.setEvaluation_comments('{'+selfInterview.getDescription()+'}');
            interviewRecords.setAi_evaluation_score(selfInterview.getScore());
            // 创建更新条件
            QueryWrapper<InterviewRecords> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("interview_id", interviewRecords.getInterview_id()).eq("record_id", aiInterviewMessage.getRecord_id());
            interviewRecordsMapper.update(interviewRecords, queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtils.success(result);
    }

    /**
     * 一对一 AI 面试对话接口,回答ai生成的问题
     *
     * @param aiInterviewMessage 候选人的回答信息
     * @return AI 面试官的回复
     */
    @PostMapping("/answer_conversation")
    public BaseResponse<String> answerInterviewConversation(@RequestBody AiInterviewMessage aiInterviewMessage) {
        InterviewRecords interviewRecords1 = interviewRecordsMapper.selectById(aiInterviewMessage.getRecord_id());
        // 使用稳定的对话方式启动一对一面试
        String result = aiManager.answerStableInterview(aiInterviewMessage.getUserMessage(),interviewRecords1.getQuestion());
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ApiResponse response = objectMapper.readValue(result, ApiResponse.class);
            System.out.println("Message Content: " + response.getMessage().getContent());
            SelfInterview selfInterview = objectMapper.readValue(response.getMessage().getContent(), SelfInterview.class);
            System.out.println("Self Interview"+selfInterview);
            InterviewRecords interviewRecords = new InterviewRecords();
            interviewRecords.setInterview_id(aiInterviewMessage.getInterview_id());
            interviewRecords.setCandidate_response(interviewRecords1.getCandidate_response() + aiInterviewMessage.getUserMessage());
            interviewRecords.setEvaluation_comments(interviewRecords1.getEvaluation_comments() + '{' +selfInterview.getDescription()+ '}');
            interviewRecords.setAi_evaluation_score(selfInterview.getScore()+interviewRecords1.getAi_evaluation_score());
            // 创建更新条件
            QueryWrapper<InterviewRecords> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("interview_id", interviewRecords.getInterview_id()).eq("record_id", aiInterviewMessage.getRecord_id());
            interviewRecordsMapper.update(interviewRecords, queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtils.success(result);
    }

    /**
     * 一对一 AI 面试对话接口,面试者问ai问题
     *
     * @param aiInterviewMessage 候选人的回答信息
     * @return AI 面试官的回复
     */
    @PostMapping("/final_conversation")
    public BaseResponse<String> finalInterviewConversation(@RequestBody AiInterviewMessage aiInterviewMessage) {
        InterviewRecords interviewRecords1 = interviewRecordsMapper.selectById(aiInterviewMessage.getRecord_id());
        // 使用稳定的对话方式启动一对一面试
        String result = aiManager.finalStableInterview(aiInterviewMessage.getUserMessage());
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ApiResponse response = objectMapper.readValue(result, ApiResponse.class);
            System.out.println("Message Content: " + response.getMessage().getContent());
            if (response.getMessage().getContent().startsWith("```json")){
                response.getMessage().setContent(response.getMessage().getContent().substring(7,response.getMessage().getContent().length()-3));
            }
            SelfInterview selfInterview = objectMapper.readValue(response.getMessage().getContent(), SelfInterview.class);
            System.out.println("Self Interview"+selfInterview);
            InterviewRecords interviewRecords = new InterviewRecords();
            interviewRecords.setInterview_id(aiInterviewMessage.getInterview_id());
            interviewRecords.setCandidate_response(interviewRecords1.getCandidate_response() + aiInterviewMessage.getUserMessage());
            interviewRecords.setEvaluation_comments(interviewRecords1.getEvaluation_comments() +  '{' +selfInterview.getDescription()+ '}');
            interviewRecords.setAi_evaluation_score(selfInterview.getScore()+interviewRecords1.getAi_evaluation_score());
            // 创建更新条件
            QueryWrapper<InterviewRecords> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("interview_id", interviewRecords.getInterview_id()).eq("record_id", aiInterviewMessage.getRecord_id());
            interviewRecordsMapper.update(interviewRecords, queryWrapper);
            return ResultUtils.success(response.getMessage().getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtils.error(OPERATION_ERROR);
    }

    @PostMapping("/get/interview_record")
    public BaseResponse<InterviewSummary> getInterviewRecord(@RequestBody AiInterviewMessage aiInterviewMessage) {
        Long interviewId = aiInterviewMessage.getInterview_id();
        Integer recordId = aiInterviewMessage.getRecord_id();
        QueryWrapper<InterviewRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interview_id", interviewId).eq("record_id", recordId);
        InterviewRecords interviewRecords = interviewRecordsMapper.selectOne(queryWrapper);
        InterviewSummary interviewSummary = new InterviewSummary();
        interviewSummary.setInterviewRecords(interviewRecords);
        String result = aiManager.applyRecommend("面试问题:"+interviewRecords.getQuestion()+" 面试者回答:"+interviewRecords.getCandidate_response()+
                " ai面试官的回答:"+interviewRecords.getEvaluation_comments());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ApiResponse response = objectMapper.readValue(result, ApiResponse.class);
            System.out.println("Message Content: " + response.getMessage().getContent());
            InterviewFeedback interviewFeedback = objectMapper.readValue(response.getMessage().getContent(), InterviewFeedback.class);
            System.out.println("Self Interview"+interviewFeedback);
            interviewFeedback.setInterview_id(interviewRecords.getInterview_id());
            interviewFeedback.setRecord_id(recordId);
            interviewFeedbackMapper.insert(interviewFeedback);
            interviewSummary.setInterviewFeedback(interviewFeedback);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtils.success(interviewSummary);
    }


    /**
     * 实时 AI 面试对话接口（流式）
     *
     * @param userMessage 候选人的回答信息
     */
    @PostMapping("/start_streaming_conversation")
    public void startStreamingInterviewConversation(@RequestBody String userMessage) {
        // 调用流式请求，使用流来实现实时对话
        Flowable<ModelData> flowable = aiManager.startStreamingInterview(userMessage, 0.7f);
        flowable.subscribe(new DefaultSubscriber<ModelData>() {
            @Override
            public void onNext(ModelData modelData) {
                System.out.println("AI 回复: " + modelData.getModel());
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("对话出错: " + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("面试对话结束。");
            }
        });
    }

    /**
     * 工具方法：从 JSON 中解析面试题目
     *
     * @param json 面试题目 JSON 字符串
     * @return 面试题目列表
     */
    private List<QuestionContentDTO> parseQuestionsFromJson(String json) {
        return JSONUtil.toList(json, QuestionContentDTO.class);
    }


    /**
     * 生成面试题目的用户消息
     *
     * @param position
     * @param questionNumber
     * @param optionNumber
     * @return
     */
    private String getGenerateInterviewQuestionUserMessage(Positions position, int questionNumber, int optionNumber) {
        StringBuilder userMessage = new StringBuilder();
        userMessage.append(position.getPosition_name()).append("\n");
        userMessage.append(position.getPosition_desc()).append("\n");
        userMessage.append(PositionTypeEnum.getEnumByValue(position.getPosition_type()).getText() + "类").append("\n");
        userMessage.append(questionNumber).append("\n");
        userMessage.append(optionNumber);
        return userMessage.toString();
    }

}
