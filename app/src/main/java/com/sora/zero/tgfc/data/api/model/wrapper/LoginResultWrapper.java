package com.sora.zero.tgfc.data.api.model.wrapper;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by zsy on 4/10/17.
 */

public class LoginResultWrapper extends BaseWrapper {
    private boolean isLoginSuccess;

    public void setLoginSuccess(boolean loginSuccess) {
        isLoginSuccess = loginSuccess;
    }

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }

    public static LoginResultWrapper fromSource(String source) {
        source = StringEscapeUtils.unescapeHtml4(source);
        LoginResultWrapper loginResultWrapper = new LoginResultWrapper();

        if(source.contains("成功登录")) {
            loginResultWrapper.setLoginSuccess(true);
            return loginResultWrapper;
        } else {
            loginResultWrapper.setErrorInfo("登陆失败", ERROR_TYPE_OTHERS);
            return loginResultWrapper;
        }
    }

}
