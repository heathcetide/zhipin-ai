package com.zhi.pin.model.dto.user;

import java.io.Serializable;

/**
 * 用户登录请求
 *
 */
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String userPassword;

    private String hcaptchaResponse;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getHcaptchaResponse() {
        return hcaptchaResponse;
    }

    public void setHcaptchaResponse(String hcaptchaResponse) {
        this.hcaptchaResponse = hcaptchaResponse;
    }

    @Override
    public String toString() {
        return "UserLoginRequest{" +
                "userAccount='" + userAccount + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", hcaptchaResponse='" + hcaptchaResponse + '\'' +
                '}';
    }
}
