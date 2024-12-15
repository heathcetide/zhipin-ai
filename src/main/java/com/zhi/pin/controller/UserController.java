package com.zhi.pin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhi.pin.annotation.AuthCheck;
import com.zhi.pin.common.BaseResponse;
import com.zhi.pin.common.DeleteRequest;
import com.zhi.pin.common.ErrorCode;
import com.zhi.pin.common.ResultUtils;
import com.zhi.pin.config.WxOpenConfig;
import com.zhi.pin.constant.UserConstant;
import com.zhi.pin.exception.BusinessException;
import com.zhi.pin.exception.ThrowUtils;
import com.zhi.pin.mapper.UserMapper;
import com.zhi.pin.model.dto.user.*;
import com.zhi.pin.model.entity.User;
import com.zhi.pin.model.vo.LoginUserVO;
import com.zhi.pin.model.vo.UserVO;
import com.zhi.pin.service.UserService;
import com.zhi.pin.utils.CaptchaUtils;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zhi.pin.constant.UserConstant.USER_LOGIN_STATE;
import static com.zhi.pin.model.enums.UserRoleEnum.USER;

/**
 * 用户接口
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private WxOpenConfig wxOpenConfig;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserMapper userMapper;

    // region 登录相关
    @GetMapping("/captcha")
    public void getCaptcha(HttpSession session, HttpServletResponse response) throws IOException {
        String sessionId = session.getId();
        System.out.println("Session ID (getCaptcha): " + sessionId);
        String captchaText = CaptchaUtils.generateCaptchaText(6);
        System.out.println("Generated Captcha Text: " + captchaText);
        session.setAttribute("captcha", captchaText);

        // 生成验证码图片
        BufferedImage image = CaptchaUtils.generateCaptchaImage(captchaText);
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // 禁用缓存
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        // 将图片写入响应流
        ImageIO.write(image, "png", response.getOutputStream());
    }

    @PostMapping("/verify-captcha")
    public BaseResponse<Boolean> verifyCaptcha(@RequestBody Map<String, String> request, HttpSession session) {
        String sessionId = session.getId();
        System.out.println("Session ID (verifyCaptcha): " + sessionId);
        String userCaptcha = request.get("captcha");
        String sessionCaptcha = (String) session.getAttribute("captcha");
        System.out.println("User Captcha: " + userCaptcha + " Session Captcha: " + sessionCaptcha);
        boolean success = userCaptcha != null && userCaptcha.equalsIgnoreCase(sessionCaptcha);
        System.out.println("Verification Result: " + success);
        return ResultUtils.success(success);
    }

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {

        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户登录（微信开放平台）
     */
    @GetMapping("/login/wx_open")
    public BaseResponse<LoginUserVO> userLoginByWxOpen(HttpServletRequest request, HttpServletResponse response,
                                                       @RequestParam("code") String code) {
        WxOAuth2AccessToken accessToken;
        try {
            WxMpService wxService = wxOpenConfig.getWxMpService();
            accessToken = wxService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo userInfo = wxService.getOAuth2Service().getUserInfo(accessToken, code);
            String unionId = userInfo.getUnionId();
            String mpOpenId = userInfo.getOpenid();
            if (StringUtils.isAnyBlank(unionId, mpOpenId)) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败，系统错误");
            }
            return ResultUtils.success(userService.userLoginByMpOpen(userInfo, request));
        } catch (Exception e) {
//            log.error("userLoginByWxOpen error", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败，系统错误");
        }
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);

        // 添加用户访问记录
        try {
            addUserVisit(user.getUserName());
        } catch (Exception e) {
            System.err.println("Failed to add user visit: " + e.getMessage());
        }

        // 获取估算访问次数
        Long estimatedVisits = getEstimatedVisits();
        System.out.println("Estimated visits: " + estimatedVisits);

        // 合并每日的访问记录
        String dailyKey = "user_visits:" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        mergeUserVisits("user_visits", dailyKey);

        // 获取合并后的访问次数
        Long totalEstimatedVisits = (Long) redisTemplate.execute((RedisConnection connection) ->
                connection.pfCount("total_user_visits".getBytes())
        );
        System.out.println("Total estimated visits: " + totalEstimatedVisits);

        return ResultUtils.success(userService.getLoginUserVO(user));
    }
    // endregion

    // region 增删改查
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }
    /**
     * 创建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        user.setUserPassword("123456");
        user.setUserRole(String.valueOf(USER));
        user.setGender("F");
        int result = userMapper.insert(user);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 用户签到（需登录）
     * @param request
     * @return
     */
    @PostMapping("/sign")
    public BaseResponse<Boolean> userSign(HttpServletRequest request) {
        return userService.sign(request);
    }

    /**
     * 获取当前签到天数
     * @param request
     * @return
     */
    @GetMapping("/sign/count")
    public BaseResponse<Integer> getUserSignCount(HttpServletRequest request) {
        return userService.getUserSignCount(request);
    }

    /**
     * 获取连续签到天数
     * @param request
     * @return
     */
    @GetMapping("/sign/continue")
    public BaseResponse<Integer> getUserSignContinue(HttpServletRequest request) {
        return userService.getUserContinuousSignCount(request);
    }
    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                   HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                       HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    // endregion

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
                                              HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String redisKey = String.format("zhipin:user:recommend:%s", loginUser.getId());
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 如果有缓存，直接读缓存
        Page<User> userPage = (Page<User>) valueOperations.get(redisKey);
        if (userPage != null) {
            return ResultUtils.success(userPage);
        }

        // 无缓存，查数据库
        QueryWrapper<User> query = new QueryWrapper<>();
        Page<User> userList = userService.page(new Page<>((pageNum-1) * pageSize, pageSize),query);
        // 写缓存
        try {
            valueOperations.set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.out.println("redis set key error"+e);
        }

        return ResultUtils.success(userList);
    }
    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num, HttpServletRequest request) {
        if (num <= 0 || num > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.matchUsers(num, user));
    }

    @GetMapping(value = "/search/tags")
    public BaseResponse<List<User>> searchUserByTags(@RequestParam(required = false) List<String> tagsList) {
        if (CollectionUtils.isEmpty(tagsList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUserByTags(tagsList);
        System.out.println("SQL方式："+userList);
        return ResultUtils.success(userList);
    }

    @PostMapping("/update/pwd")
    public BaseResponse<Boolean> updateUserPwd(@RequestBody UserUpdatePwd userUpdatePwd, HttpServletRequest request) {
        if (userUpdatePwd == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.updateUserPwd(userUpdatePwd, loginUser));
    }
    private void addUserVisit(String userName) {
        redisTemplate.execute((RedisConnection connection) -> {
            connection.pfAdd("user_visits".getBytes(), userName.getBytes());
            return null;
        });
    }

    private Long getEstimatedVisits() {
        return (Long) redisTemplate.execute((RedisConnection connection) ->
                connection.pfCount("user_visits".getBytes())
        );
    }

    private void mergeUserVisits(String... keys) {
        redisTemplate.execute((RedisConnection connection) -> {
            connection.pfMerge("total_user_visits".getBytes(),
                    Arrays.stream(keys).map(String::getBytes).toArray(byte[][]::new));
            return null;
        });
    }


    @GetMapping("/uv/track")
    public String trackUserVisit(HttpServletRequest request) {
        // 获取用户唯一标识
        String userId = getUserUniqueId(request);

        // 生成每日 UV 键
        String dailyKey = "uv:" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 添加用户访问记录到当天的 UV 统计
        redisTemplate.execute((RedisConnection connection) -> {
            connection.pfAdd(dailyKey.getBytes(), userId.getBytes());
            return null;
        });

        return "User visit tracked for key: " + dailyKey;
    }

    @GetMapping("/uv/count")
    public String getUVCount() {
        // 获取今日 UV 统计键
        String dailyKey = "uv:" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 查询今日 UV 数量
        Long uvCount = (Long) redisTemplate.execute((RedisConnection connection) ->
                connection.pfCount(dailyKey.getBytes())
        );

        return "Today's UV: " + uvCount;
    }

    @GetMapping("/uv/total")
    public String getTotalUVCount() {
        // 模拟需要统计的日期范围
        String[] days = {"uv:2024-12-01", "uv:2024-12-02", "uv:2024-12-03"};

        // 合并多天的 UV 数据
        redisTemplate.execute((RedisConnection connection) -> {
            connection.pfMerge("uv:total".getBytes(),
                    Arrays.stream(days).map(String::getBytes).toArray(byte[][]::new));
            return null;
        });

        // 查询合并后的总 UV 数量
        Long totalUVCount = (Long) redisTemplate.execute((RedisConnection connection) ->
                connection.pfCount("uv:total".getBytes())
        );

        return "Total UV: " + totalUVCount;
    }

    private String getUserUniqueId(HttpServletRequest request) {
        // 模拟通过请求获取用户唯一标识，这里可以替换为实际逻辑
        return request.getSession().getId(); // 使用 Session ID 作为示例
    }
}