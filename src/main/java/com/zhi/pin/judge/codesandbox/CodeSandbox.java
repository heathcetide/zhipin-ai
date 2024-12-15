package com.zhi.pin.judge.codesandbox;

import com.zhi.pin.judge.codesandbox.model.ExecuteCodeRequest;
import com.zhi.pin.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
