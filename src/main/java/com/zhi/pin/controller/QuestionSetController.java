package com.zhi.pin.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.zhi.pin.common.BaseResponse;
import com.zhi.pin.common.ErrorCode;
import com.zhi.pin.common.ResultUtils;
import com.zhi.pin.exception.BusinessException;
import com.zhi.pin.exception.ThrowUtils;
import com.zhi.pin.model.dto.questionSet.QuestionSetAddRequest;
import com.zhi.pin.model.dto.questionSet.QuestionSetQueryRequest;
import com.zhi.pin.model.entity.QuestionSet;
import com.zhi.pin.model.entity.User;
import com.zhi.pin.model.vo.QuestionSetVO;
import com.zhi.pin.service.QuestionSetService;
import com.zhi.pin.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/question-set")
public class QuestionSetController {

    @Resource
    private QuestionSetService questionSetService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    /**
     * 创建
     *
     * @param questionSetAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionSetAddRequest questionSetAddRequest, HttpServletRequest request) {
        if (questionSetAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSet questionSet = new QuestionSet();
        BeanUtils.copyProperties(questionSetAddRequest, questionSet);
        List<String> tags = questionSetAddRequest.getQuestionIds();
        if (tags != null) {
            questionSet.setQuestionIds(GSON.toJson(tags));
        }
        questionSetService.validQuestionSet(questionSet, true);
        User loginUser = userService.getLoginUser(request);
        questionSet.setUserId(loginUser.getId());
        questionSet.setUpdateTime(new Date());
        questionSet.setCreateTime(new Date());
        questionSet.setIsDelete(0);
        boolean result = questionSetService.save(questionSet);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionId = questionSet.getId();
        return ResultUtils.success(newQuestionId);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionSetQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionSetVO>> listQuestionVOByPage(@RequestBody QuestionSetQueryRequest questionSetQueryRequest,
                                                                  HttpServletRequest request) {
        long current = questionSetQueryRequest.getCurrent();
        long size = questionSetQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<QuestionSet> questionPage = questionSetService.page(new Page<>(current, size),
                questionSetService.getQueryWrapper(questionSetQueryRequest));
        return ResultUtils.success(questionSetService.getQuestionVOPage(questionPage, request));
    }
}
