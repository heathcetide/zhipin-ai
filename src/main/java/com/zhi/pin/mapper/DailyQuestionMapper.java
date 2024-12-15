package com.zhi.pin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhi.pin.model.entity.DailyQuestion;
import org.apache.ibatis.annotations.Select;

public interface DailyQuestionMapper extends BaseMapper<DailyQuestion> {

    @Select("select * from daily_question where date = #{date}")
    DailyQuestion selectByDate(String date);
}
