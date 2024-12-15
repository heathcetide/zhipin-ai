// region AI 面试一对一交互及题目生成功能
package com.zhi.pin.manager;

import com.zhi.pin.common.ErrorCode;
import com.zhi.pin.exception.BusinessException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * AI 面试平台的通用 AI 调用管理器
 */
@Component
public class AiManager {

    @Resource
    private ClientV4 clientV4;

    // 稳定的随机数
    private static final float STABLE_TEMPERATURE = 0.05f;

    // 不稳定的随机数
    private static final float UNSTABLE_TEMPERATURE = 0.99f;

    private static final float LITTLE_UNSTABLE_TEMPERATURE = 0.10f;
    /**
     * 同步请求生成面试题目（答案较稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String generateStableInterviewQuestions(String systemMessage, String userMessage) {
        return doRequest(systemMessage, userMessage, Boolean.FALSE,LITTLE_UNSTABLE_TEMPERATURE);
    }

    /**
     * 一对一面试会话（答案较稳定）
     *
     * @param userMessage
     * @return
     */
    public String startStableInterview(String userMessage) {
        String systemMessage = "你是一个严谨且有礼貌的面试官，面试开始了，请根据候选人的回答做出相应的提问。当前是面试者的自我介绍,请分析并给出面试评价和打分，" +
                "1. 要求：返回的格式必须是JSON字符串\n" +
                "2. 严格按照下面的 json 格式输出评价和打分,打分满分100分\n" +
                "{\"description\":\"自我介绍评价的内容\",\"score\":\"90\"}\n" +
                "3. 不需要返回给我其他的描述，一定要是固定格式的JSON字符串且只需要一个description和一个score，不要多个这样的json字符串" +
                "4. 请扮演一个严格的面试官，不要要求太低，分太高" +
                "5. 不要用```json```包裹";
        return doRequest(systemMessage, userMessage, Boolean.FALSE, STABLE_TEMPERATURE);
    }

    /**
     * 一对一面试会话（答案较稳定）,给ai问题打分
     *
     * @param userMessage
     * @return
     */
    public String answerStableInterview(String userMessage,String question) {
        String systemMessage = "你是一个严谨且有礼貌的面试官，面试开始了，请根据候选人的回答做出相应的提问。当前是面试者回答ai面试官的技术问题,请分析并给出这一阶段面试评价和打分，" +
                "1. 要求：返回的格式必须是JSON字符串\n" +
                "2. 严格按照下面的 json 格式输出评价和打分,打分满分100分\n" +
                "{\"description\":\"AI面试答题分数\",\"score\":\"90\"}\n" +
                "3. 不需要返回给我其他的描述，一定要是固定格式的JSON字符串，我只要{\"description\":\"AI面试答题分数\",\"score\":\"0\"}这样的回答" +
                "4. 请扮演一个严格的面试官，不要要求太低，分太高" +
                "5. 无论面试者给出什么回答,只需要给我相应的json格式打分字符串,如果面试者答题混乱,则给出零分" +
                "5. 请不要用```json```包裹" +
                "6. 不要出现下面这种文字: 如果面试者给出了以下回答：回答: 使用Spring Boot，我们可以通过添加spring-boot-starter-web依赖来创建一个RESTful API。" +
                "题目如下:";
        return doRequest(systemMessage+question, userMessage, Boolean.FALSE, STABLE_TEMPERATURE);
    }

    /**
     * 一对一面试会话（答案较稳定）
     *
     * @param userMessage
     * @return
     */
    public String finalStableInterview(String userMessage) {
        String systemMessage = "你是一个严谨且有礼貌的面试官，面试开始了，请根据候选人的回答做出相应的提问。当前是面试者问ai面试官的流程,针对面试者的问题进行回答,请分析面试者的问题的分量并给出面试评价和打分，" +
                "1. 要求：返回的格式必须是JSON字符串\n" +
                "2. 请非常严格的按照下面的 json 格式输出评价和打分,满分为100分\n" +
                "{\"description\":\"对面试者问题评价的内容\",\"score\":\"90\"}\n" +
                "3. 不需要返回给我其他的描述，一定要是固定格式的JSON字符串，我只要{\"description\":\"对面试者问题评价的内容\",\"score\":\"0\"}这样的回答" +
                "4. 请扮演一个严格的面试官，不要要求太低，分太高" +
                "5. 无论面试者给出什么回答,只需要给我相应的json格式打分字符串,如果面试者答题混乱,则给出零分" +
                "6. 请不要出现```json```这种格式！ 请不要出现```json```这种格式！ 请不要出现```json```这种格式！" +
                "7. 请不要出现下面这种文字:题目 3: 请简述MySQL中索引的作用及使用场景。回答: 索引可以加快数据检索速度，通过优化查询性能来提高数据库的整体性能";
        return doRequest(systemMessage, userMessage, Boolean.FALSE, STABLE_TEMPERATURE);
    }

    public String applyRecommend(String userMessage) {
        String systemMessage = "你是一个严谨且有礼貌的面试官，面试结束了，请根据候选人的回答做出相应的反馈。当前是面试官给面试者提供一些建议的部分，将会根据面试的问题，面试者的回答，和面试官的反馈三个方面进行分析，并分析强项，若想，建议，方向" +
                "1. 要求：返回的格式必须是JSON字符串\n" +
                "2. 请非常严格的按照下面的 json 格式输出评价和打分,满分为100分\n" +
                "{\"strengths\":\"面试者的强项\",\"weaknesses\":\"面试者的弱项\",\"recommendations\":\"给面试者的建议\",\"follow_up_actions\":\"面试者的努力方向\"}\n" +
                "3. 不需要返回给我其他的描述，一定要是固定格式的JSON字符串，我只要{\"description\":\"对面试者问题评价的内容\",\"score\":\"0\"}这样的回答" +
                "4. 请扮演一个严格的面试官，不要要求太低，分太高" +
                "5. 无论面试者给出什么回答,只需要给我相应的json格式打分字符串,如果面试者答题混乱,则给出零分" +
                "6. 请不要用```json```包裹" +
                "7. 请不要出现下面这种文字:题目 3: 请简述MySQL中索引的作用及使用场景。回答: 索引可以加快数据检索速度，通过优化查询性能来提高数据库的整体性能";
        return doRequest(systemMessage, userMessage, Boolean.FALSE, STABLE_TEMPERATURE);
    }

    /**
     * 一对一面试会话（答案不稳定）
     *
     * @param userMessage
     * @return
     */
    public String startUnstableInterview(String userMessage) {
        String systemMessage = "你是一个具有创新性和开放性的面试官，可以随机提出有挑战性的问题。";
        return doRequest(systemMessage, userMessage, Boolean.FALSE, UNSTABLE_TEMPERATURE);
    }

    /**
     * 通用请求（简化消息传递）
     *
     * @param systemMessage
     * @param userMessage
     * @param stream
     * @param temperature
     * @return
     */
    private String doRequest(String systemMessage, String userMessage, Boolean stream, Float temperature) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        chatMessageList.add(systemChatMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        chatMessageList.add(userChatMessage);
        return doRequest(chatMessageList, stream, temperature);
    }
    /**
     * 通用请求
     *
     * @param messages
     * @param stream
     * @param temperature
     * @return
     */
    private String doRequest(List<ChatMessage> messages, Boolean stream, Float temperature) {
        // 构建请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .maxTokens(120)
                .temperature(9.0F)
                .stream(Boolean.FALSE)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        try {
            ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
            return invokeModelApiResp.getData().getChoices().get(0).toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }

    /**
     * 流式请求用于实时会话的实现
     *
     * @param userMessage
     * @param temperature
     * @return
     */
    public Flowable<ModelData> startStreamingInterview(String userMessage, Float temperature) {
        String systemMessage = "你是一个有耐心且专业的面试官，候选人将与你进行实时互动，请根据他的回答来提问。";
        return doStreamRequest(systemMessage, userMessage, temperature);
    }

    /**
     * 通用流式请求（简化消息传递）
     *
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    private Flowable<ModelData> doStreamRequest(String systemMessage, String userMessage, Float temperature) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        chatMessageList.add(systemChatMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        chatMessageList.add(userChatMessage);
        return doStreamRequest(chatMessageList, temperature);
    }

    /**
     * 通用流式请求
     *
     * @param messages
     * @param temperature
     * @return
     */
    private Flowable<ModelData> doStreamRequest(List<ChatMessage> messages, Float temperature) {
        // 构建请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.TRUE)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        try {
            ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
            return invokeModelApiResp.getFlowable();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }
}
