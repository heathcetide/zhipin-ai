package com.zhi.pin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhi.pin.constant.CommonConstant;
import com.zhi.pin.mapper.QuestionSetMapper;
import com.zhi.pin.model.dto.questionSet.QuestionSetQueryRequest;
import com.zhi.pin.model.entity.QuestionSet;
import com.zhi.pin.model.entity.User;
import com.zhi.pin.model.vo.QuestionSetVO;
import com.zhi.pin.service.QuestionSetService;
import com.zhi.pin.service.UserService;
import com.zhi.pin.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Lenovo
* @description 针对表【question_set(题目集)】的数据库操作Service实现
* @createDate 2024-10-12 23:14:52
*/
@Service
public class QuestionSetServiceImpl extends ServiceImpl<QuestionSetMapper, QuestionSet>
implements QuestionSetService {

    @Override
    public void validQuestionSet(QuestionSet questionSet, boolean b) {
        if (questionSet == null){
            throw new RuntimeException("数据不存在");
        }
        String description = questionSet.getDescription();
        String name = questionSet.getName();
        String questionIds = questionSet.getQuestionIds();
        if (b && (description == null || description.length() == 0)){
            throw new RuntimeException("描述不能为空");
        }
        if (name == null || name.length() == 0){
            throw new RuntimeException("名称不能为空");
        }
        if (questionIds == null || questionIds.length() == 0){
            throw new RuntimeException("题目id不能为空");
        }
        if (questionIds.length() > 255){
            throw new RuntimeException("题目id长度不能超过255");
        }
        if (name.length() > 50){
            throw new RuntimeException("名称长度不能超过50");
        }
        if (description.length() > 1024){
            throw new RuntimeException("描述长度不能超过1024");
        }
    }

    @Resource
    private UserService userService;

    @Override
    public Page<QuestionSetVO> getQuestionVOPage(Page<QuestionSet> questionPage, HttpServletRequest request) {
        List<QuestionSet> questionList = questionPage.getRecords();
        Page<QuestionSetVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(QuestionSet::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<QuestionSetVO> questionVOList = questionList.stream().map(question -> {
            QuestionSetVO questionSetVO = QuestionSetVO.objToVo(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionSetVO.setUserVO(userService.getUserVO(user));
            return questionSetVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Override
    public Wrapper<QuestionSet> getQueryWrapper(QuestionSetQueryRequest questionSetQueryRequest) {
        QueryWrapper<QuestionSet> queryWrapper = new QueryWrapper<>();
        if (questionSetQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionSetQueryRequest.getId();
        String title = questionSetQueryRequest.getName();
        String content = questionSetQueryRequest.getDescription();
        List<String> tags = questionSetQueryRequest.getTags();
        Long userId = questionSetQueryRequest.getUserId();
        String sortField = questionSetQueryRequest.getSortField();
        String sortOrder = questionSetQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "name", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "description", content);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}
