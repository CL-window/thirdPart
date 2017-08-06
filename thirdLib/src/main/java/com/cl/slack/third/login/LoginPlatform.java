package com.cl.slack.third.login;

import android.content.Context;

import com.cl.slack.third.Platform;


/**
 * created by slack
 * on 17/7/28 下午2:08
 */

public abstract class LoginPlatform extends Platform {

    public enum LoginState {
        STATE_SHARE,//  分享
        STATE_LOGIN,//  登录
        // 绑定, 更换 绑定 帐号 都是调用绑定接口
        STATE_BIND,// 绑定
    }

    public LoginState mLoginState = LoginState.STATE_SHARE;

    public LoginPlatform() {

    }

    public void login(Context context, LoginState state) {
        mLoginState = state;
        loginByChild(context);
    }

    protected abstract void loginByChild(Context context);

}
