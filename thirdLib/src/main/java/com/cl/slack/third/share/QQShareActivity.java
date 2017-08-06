package com.cl.slack.third.share;

import android.os.Bundle;
import android.text.TextUtils;

import com.cl.slack.third.BaseQQActivity;
import com.cl.slack.third.ThirdParty;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.UiError;

/**
 * Created by slack
 * on 17/7/27 上午11:54
 * doc: http://wiki.open.qq.com/wiki/mobile/API%E8%B0%83%E7%94%A8%E8%AF%B4%E6%98%8E#1.13_.E5.88.86.E4.BA.AB.E6.B6.88.E6.81.AF.E5.88.B0QQ.EF.BC.88.E6.97.A0.E9.9C.80QQ.E7.99.BB.E5.BD.95.EF.BC.89
 *
 * QQ 分享时，取消并不会被回调，分享成功不返回我们的应用，也不会回调成功
 * 解决：简单粗暴一点，判断是不是第二次进 onResume
 */

public class QQShareActivity extends BaseQQActivity {

    private ThirdSharePlatform mPlatform;
    private boolean mInResumeAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlatform = ThirdParty.getSharePlatform(QQSharePlatform.NAME);
        sendMultiMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mInResumeAgain) {
            mInResumeAgain = true;
        } else {
            finishWithError();
        }
    }

    private void sendMultiMessage() {
        if(mPlatform != null) {
            ShareParams params = mPlatform.getShareParams();
            if(params != null) {
                Bundle bundle = new Bundle();
                String imagePath = params.imagePath;
                if(params.sharePic() && !TextUtils.isEmpty(imagePath)) {
                    bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                    bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath);
                    mTencent.shareToQQ(this, bundle, this);
                    return;
                } else if(params.shareVideo()){
                    if(params instanceof QQSharePlatform.QQShareParams) {
                        QQSharePlatform.QQShareParams qqShareParams = (QQSharePlatform.QQShareParams) params;
                        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, qqShareParams.title);
                        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, qqShareParams.actionUrl);
                        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, qqShareParams.text);
                        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, qqShareParams.thumbUrl);
                        mTencent.shareToQQ(this, bundle, this);
                        return;
                    }
                }
            }
        }
        finishWithError();
    }

    @Override
    public void onComplete(Object o) {
        if(mPlatform != null) {
            mPlatform.onComplete();
        }
        finishWithSuccess();
    }

    @Override
    public void onError(UiError uiError) {
        if(mPlatform != null) {
            mPlatform.onError(uiError.toString());
        }
        finishWithError();
    }

    @Override
    public void onCancel() {
        if(mPlatform != null) {
            mPlatform.onCancel();
        }
        finishWithError();
    }
}
