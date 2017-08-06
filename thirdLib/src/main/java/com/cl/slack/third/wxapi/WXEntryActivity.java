package com.cl.slack.third.wxapi;

import android.os.Bundle;
import android.util.Log;

import com.cl.slack.third.BaseWXActivity;
import com.cl.slack.third.ThirdParty;
import com.cl.slack.third.login.LoginPlatform;
import com.cl.slack.third.login.WXLoginPlatform;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

/**
 * TODO : 微信登录时会停留在此界面一段时间
 * TODO ：oppo meitu 微信没有登陆状态下，登陆界面返回 不会触发 onResp ！！！ 问题存在于微信刚下载或者未登陆过帐号，登陆帐号然后退出是可以触发的
 * 此界面目前只是微信登录
 * weixin will call WXEntryActivity this
 * @author slack
 * on 17/3/23 下午12:41
 */
public class WXEntryActivity extends BaseWXActivity {

    private final String TAG = "WXEntryActivity";
    private boolean mInResumeAgain; // 微信如果回调 先 进回调onResp 如果没有finish 才会进 onResume
    LoginPlatform mLoginPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginPlatform = ThirdParty.getLoginPlatform(WXLoginPlatform.NAME);
        if(mLoginPlatform == null) {
            finishWithError();
            return;
        }
        if(mLoginPlatform.mLoginState != LoginPlatform.LoginState.STATE_SHARE) {
            openWeixin();
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume... mInResumeAgain  " + mInResumeAgain);
        super.onResume();
        if(!mInResumeAgain) {
            mInResumeAgain = true;
        } else {
            finishWithError();
        }

    }

    /**
     * 应用请求微信的响应结果将通过onResp回调
     */
    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
        Log.i(TAG, "onResp, errCode:" + resp.errCode + " type:" + resp.getType());

        /** 微信登录 */
        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            String code = ((SendAuth.Resp) resp).code;
            Log.i(TAG, "code = " + code);
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    mInResumeAgain = false;
                    getUserInfo(code);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    finishWithError();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    finishWithError();
                    break;
                default:
                    finishWithError();
                    break;
            }
        } else {
            finishWithError();
        }
    }

    /**
     * 微信登录，
     * 微信绑定
     */
    private void getUserInfo(String code) {

        if(mLoginPlatform != null) {
            mLoginPlatform.onComplete(code);
        }
        finishWithSuccess();

    }

}
