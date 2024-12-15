package com.zhi.pin;

import cn.hutool.json.JSONUtil;
import com.zhi.pin.model.dto.QuestionContentDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JSONParse {

    @Test
    public void test(){
//        String json = "[{\\\"options\\\":[],\\\"title\\\":\\\"如何使用Spring Boot实现一个RESTful API？\\\"},\\n{\\\"options\\\":[],\\\"title\\\":\\\"在SSM框架中，如何实现事务管理？\\\"},\\n{\\\"options\\\":[],\\\"title\\\":\\\"请简述MySQL中索引的作用及使用场景。\\\"},\\n{\\\"options\\\":[],\\\"title\\\":\\\"Redis中如何实现分布式锁？\\\"},\\n{\\\"options\\\":[],\\\"title\\\":\\\"描述一下Spring Boot如何与Redis进行集成。\\\"}]";
//        String json = "[{\"options\":[],\"title\":\"如何使用Spring Boot实现一个RESTful API？\"},\n" +
//                "{\"options\":[],\"title\":\"在SSM框架中，如何实现事务管理？\"},\n" +
//                "{\"options\":[],\"title\":\"请简述MySQL中索引的作用及使用场景。\"},\n" +
//                "{\"options\":[],\"title\":\"Redis中如何实现分布式锁？\"},\n" +
//                "{\"options\":[],\"title\":\"描述一下Spring Boot如何与Redis进行集成。\"}]";
        String json = "[{\"options\":[],\"title\":\"如何使用Spring Boot实现一个RESTful API？\"},\n{\"options\":[],\"title\":\"在SSM框架中，如何实现事务管理？\"},\n{\"options\":[],\"title\":\"请简述MySQL中索引的作用以及如何优化索引？\"},\n{\"options\":[],\"title\":\"Redis中如何实现分布式锁，并说明其优缺点？\"},\n{\"options\":[],\"title\":\"在Spring Boot应用中，如何集成和使用Redis作为缓存解决方案？\"}]";
        List<QuestionContentDTO> questionContentDTOList = JSONUtil.toList(json, QuestionContentDTO.class);
        for (QuestionContentDTO questionContentDTO :questionContentDTOList) {
            System.out.println(questionContentDTO);
        }

    }
}
