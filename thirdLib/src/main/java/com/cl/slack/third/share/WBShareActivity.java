package com.cl.slack.third.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.cl.slack.third.BaseWBActivity;
import com.cl.slack.third.ThirdParty;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

/**
 * Created by slack
 * on 17/7/27 上午11:54
 * 第一次分享时，先登录授权，确保客户端处于账户登录状态
 * doc: https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/%E6%96%B0%E6%96%87%E6%A1%A3/%E5%BE%AE%E5%8D%9ASDK%204.0%E6%96%87%E6%A1%A3.pdf
 */

public class WBShareActivity extends BaseWBActivity implements WbShareCallback {

    private final static int MAX_THUMB_SIZE = 32; // 32Kb

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    private WbShareHandler mWbShareHandler;
    private ThirdSharePlatform mPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSsoHandler = new SsoHandler(this);
        mWbShareHandler = new WbShareHandler(this);
        mWbShareHandler.registerApp();
        mPlatform = ThirdParty.getSharePlatform(WBSharePlatform.NAME);
        if(AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
            sendMultiMessage();
        } else {
            mSsoHandler.authorizeClientSso(new WbAuthListener() {
                @Override
                public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                    if (oauth2AccessToken.isSessionValid()) {
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(WBShareActivity.this, oauth2AccessToken);
                        sendMultiMessage();
                    } else if (mPlatform != null) {
                        mPlatform.onError("need wb oauth2AccessToken");
                    }
                }

                @Override
                public void cancel() {
                    Log.i("slack", "WbConnect cancel...");
                    if (mPlatform != null) {
                        mPlatform.onCancel();
                    }
                    finishWithError();
                }

                @Override
                public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                    Log.i("slack", "WbConnect onFailure...");
                    if (mPlatform != null) {
                        mPlatform.onError("wbConnectErrorMessage");
                    }
                    finishWithError();
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWbShareHandler.doResultIntent(intent, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void sendMultiMessage() {
        if(mPlatform != null) {
            ShareParams params = mPlatform.getShareParams();
            if(params != null) {
                String title = params.text;
                String imagePath = params.imagePath;
                Bitmap bitmap = null;
                if (params instanceof WBSharePlatform.WBShareParams) {
                    WBSharePlatform.WBShareParams shareParams = (WBSharePlatform.WBShareParams) params;
                    bitmap = shareParams.bitmap;
                }
                WeiboMultiMessage msg = new WeiboMultiMessage();
                if (!TextUtils.isEmpty(title)) {
                    msg.textObject = getTextObj(title);
                }
                if (!TextUtils.isEmpty(imagePath)) {
                    msg.imageObject = getImageObj(imagePath);
                }
                if (bitmap != null) {
                    msg.imageObject = getImageObj(bitmap);
                }
                mWbShareHandler.shareMessage(msg, true);
                return;
            }
        }

        finishWithError();
    }

    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(String path) {
        ImageObject imageObject = new ImageObject();
        imageObject.imagePath = path;
        return imageObject;
    }

    private ImageObject getImageObj(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    @Override
    public void onWbShareSuccess() {
        if(mPlatform != null) {
            mPlatform.onComplete();
        }
        Log.i("slack", "onWbShareSuccess...");
        finishWithSuccess();
    }

    @Override
    public void onWbShareCancel() {
        if(mPlatform != null) {
            mPlatform.onCancel();
        }
        Log.i("slack", "onWbShareCancel...");
        finishWithError();
    }

    @Override
    public void onWbShareFail() {
        if(mPlatform != null) {
            mPlatform.onError("weibo error");
        }
        Log.i("slack", "onWbShareFail...");
        finishWithError();
    }
}
