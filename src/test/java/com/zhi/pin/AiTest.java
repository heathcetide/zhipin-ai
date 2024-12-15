package com.zhi.pin;

import com.zhi.pin.manager.AiManager;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AiTest {

    @Resource
    private ClientV4 clientV4;

    @Resource
    private AiManager aiManager;

    @Test
    public void test1(){
        String s = aiManager.generateStableInterviewQuestions("你是一位专业的面试题专家，我会给你如下信息：\n" +
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
                "[{\"options\":[{\"value\":\"选项内容\",\"key\":\"A\"},{\"value\":\"\",\"key\":\"B\"}],\"title\":\"题目标题\"}]\n" +
                "```\n" +
                "title 是题目，options 是选项，每个选项的 key 按照英文字母序（比如 A、B、C、D）以此类推，value 是选项内容\n" +
                "3. 检查题目是否包含序号，若包含序号则去除序号\n" +
                "4. 返回的题目列表格式必须为 JSON 数组", "java工程师");
        System.out.println(s);
    }

    @Test
    public void test() {
        // 初始化客户端
//        ClientV4 client = new ClientV4.Builder(KeyConstant.KEY).build();
        // 构建请求
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "你是一位专业的面试题专家，我会给你如下信息：\\n\" +\n" +
                "                \"```\\n\" +\n" +
                "                \"岗位名称，\\n\" +\n" +
                "                \"【【【岗位描述】】】，\\n\" +\n" +
                "                \"岗位类别，\\n\" +\n" +
                "                \"要生成的面试题目数，\\n\" +\n" +
                "                \"每个题目的选项数\\n\" +\n" +
                "                \"```\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"请你根据上述信息，按照以下步骤来生成面试题目：\\n\" +\n" +
                "                \"1. 要求：题目和选项尽可能地短，题目不要包含序号，每题的选项数以我提供的为主，题目不能重复\\n\" +\n" +
                "                \"2. 严格按照下面的 json 格式输出题目和选项\\n\" +\n" +
                "                \"```\\n\" +\n" +
                "                \"[{\\\"options\\\":[{\\\"value\\\":\\\"选项内容\\\",\\\"key\\\":\\\"A\\\"},{\\\"value\\\":\\\"\\\",\\\"key\\\":\\\"B\\\"}],\\\"title\\\":\\\"题目标题\\\"}]\\n\" +\n" +
                "                \"```\\n\" +\n" +
                "                \"title 是题目，options 是选项，每个选项的 key 按照英文字母序（比如 A、B、C、D）以此类推，value 是选项内容\\n\" +\n" +
                "                \"3. 检查题目是否包含序号，若包含序号则去除序号\\n\" +\n" +
                "                \"4. 返回的题目列表格式必须为 JSON 数组");
        messages.add(chatMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
        System.out.println("model output:" + invokeModelApiResp.getData().getChoices().get(0));
    }

}
