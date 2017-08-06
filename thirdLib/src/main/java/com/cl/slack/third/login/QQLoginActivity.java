package com.cl.slack.third.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.cl.slack.third.BaseQQActivity;
import com.cl.slack.third.ThirdParty;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * FUCK !!!  QQ 登录 需要在activity 里 onActivityResult
 * FUCK !!!  只能 extends Activity
 * FUCK !!!  这些登录操作只能在activity里，否则没有回调
 * FUCK !!!  不能使用 fast json JSONObject
 * @author slack
 * on 17/3/24 下午2:42
 */
public class QQLoginActivity extends BaseQQActivity {

    private final static String TAG = "QQLoginActivity";
    LoginPlatform mLoginPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginPlatform = ThirdParty.getLoginPlatform(QQLoginPlatform.NAME);
        if(mLoginPlatform == null) {
            finishWithError();
            return;
        }
        userQQLogin();
    }


    private void userQQLogin() {
        mTencent.login(this, "all", this);
    }

    private void doComplete(JSONObject values) {
        initOpenidAndToken(values);
        updateUserInfo();
    }

    /**
     * save token and openId
     *
     * @param jsonObject
     */
    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户信息
     */
    private void updateUserInfo() {

        if(mLoginPlatform != null) {
            mLoginPlatform.onComplete(mTencent.getOpenId(), mTencent.getAccessToken());
        }
        finishWithSuccess();
    }

    @Override
    public void onComplete(Object o) {
        // onComplete {"ret":0,"openid":"9F5C18BC81A4A0FA50D2148691B08D5B","access_token":"FD158E9ABD7589BD6D6B40BFB3AAFBC3","pay_token":"444B3D828651D9906B4387B4DB57B676","expires_in":7776000,"pf":"desktop_m_qq-10000144-android-2002-","pfkey":"569bfe8532041b1b46a4f0149c6ae6a7","msg":"","login_cost":142,"query_authority_cost":1371,"authority_cost":0}
        Log.i(TAG, "onComplete " + o.toString());
        doComplete((JSONObject) o);
    }

    @Override
    public void onError(UiError uiError) {
        Log.i(TAG, "onError " + uiError.toString());
        if(mLoginPlatform != null) {
            mLoginPlatform.onError(uiError.toString());
        }
        finishWithError();
    }

    @Override
    public void onCancel() {
        Log.i(TAG, "onCancel ");
        if(mLoginPlatform != null) {
            mLoginPlatform.onCancel();
        }
        finishWithError();
    }
}
