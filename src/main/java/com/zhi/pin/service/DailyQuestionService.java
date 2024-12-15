package com.zhi.pin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhi.pin.common.BaseResponse;
import com.zhi.pin.model.entity.DailyQuestion;
import com.zhi.pin.model.vo.DailyQuestionVO;

/**
* @author Lenovo
* @description 针对表【daily_question(每日题目记录)】的数据库操作Service
* @createDate 2024-10-12 17:26:18
*/
public interface DailyQuestionService extends IService<DailyQuestion> {

    BaseResponse<DailyQuestionVO> getDailyQuestion();
}
