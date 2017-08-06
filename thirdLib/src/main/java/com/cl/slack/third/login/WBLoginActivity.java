package com.cl.slack.third.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cl.slack.third.BaseWBActivity;
import com.cl.slack.third.ThirdParty;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

/**
 *
 * @author slack
 * on 17/3/27 下午12:52
 */
public class WBLoginActivity extends BaseWBActivity {

    private final static String TAG = "WBLoginActivity";

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    LoginPlatform mLoginPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mLoginPlatform = ThirdParty.getLoginPlatform(WBLoginPlatform.NAME);
        if(mLoginPlatform == null) {
            finishWithError();
            return;
        }
        mSsoHandler = new SsoHandler(this);
        mSsoHandler.authorize(mLoginListener);
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        } else {
            if(mLoginPlatform != null) {
                mLoginPlatform.onCancel();
            }
            finishWithError();
        }

    }

    /**
     * 登入按钮的监听器，接收授权结果。
     */
    private WbAuthListener mLoginListener = new WbAuthListener() {
        @Override
        public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
            Log.i(TAG,"onSuccess...");
            if (oauth2AccessToken != null && oauth2AccessToken.isSessionValid()) {
                // user_id: 3477341944, access_token: 2.005UZ1nDyY87OC445e6e1d0bq3pjzC, refresh_token: 2.005UZ1nDyY87OC81e61857ffl9vX8D, phone_num: , expires_in: 1491418801133
                Log.i(TAG,"Token: " + oauth2AccessToken.toString());
                if(mLoginPlatform != null) {
                    mLoginPlatform.onComplete(oauth2AccessToken.getUid(), oauth2AccessToken.getToken());
                }
                finishWithSuccess();
            }else {
                finishWithError();
            }
        }

        @Override
        public void cancel() {
            Log.i(TAG,"onCancel...");
            if(mLoginPlatform != null) {
                mLoginPlatform.onCancel();
            }
            finishWithError();
        }

        @Override
        public void onFailure(WbConnectErrorMessage e) {
            Log.e(TAG,"onFailure:  code : " + e.getErrorCode() + " msg : " + e.getErrorMessage());
            finishWithError();
        }
    };

}
