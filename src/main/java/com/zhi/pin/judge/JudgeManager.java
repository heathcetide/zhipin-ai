package com.zhi.pin.judge;

import com.zhi.pin.judge.codesandbox.model.JudgeInfo;
import com.zhi.pin.judge.strategy.DefaultJudgeStrategy;
import com.zhi.pin.judge.strategy.JavaLanguageJudgeStrategy;
import com.zhi.pin.judge.strategy.JudgeContext;
import com.zhi.pin.judge.strategy.JudgeStrategy;
import com.zhi.pin.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
