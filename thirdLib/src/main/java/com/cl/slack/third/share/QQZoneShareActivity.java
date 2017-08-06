package com.cl.slack.third.share;

import android.os.Bundle;

import com.cl.slack.third.BaseQQActivity;
import com.cl.slack.third.ThirdParty;
import com.tencent.connect.share.QzonePublish;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by slack
 * on 17/7/27 上午11:54
 * doc: com.tencent.connect.share.QzonePublish source code
 */

public class QQZoneShareActivity extends BaseQQActivity {

    private ThirdSharePlatform mPlatform;
    private boolean mInResumeAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlatform = ThirdParty.getSharePlatform(QQZoneSharePlatform.NAME);
        sendMultiMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mInResumeAgain) {
            mInResumeAgain = true;
        } else {
            finishWithError();
        }
    }

    private void sendMultiMessage() {
        if (mPlatform != null) {
            ShareParams params = mPlatform.getShareParams();
            if (params != null && params instanceof QQZoneSharePlatform.QQZoneShareParams) {
                Bundle bundle = new Bundle();
                ArrayList<String> list = new ArrayList<>();

                bundle.putString(QzonePublish.PUBLISH_TO_QZONE_APP_NAME, getPackageName());
                QQZoneSharePlatform.QQZoneShareParams zone = (QQZoneSharePlatform.QQZoneShareParams) params;
                bundle.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, zone.text);//summary
                if (params.sharePic()) {
                    bundle.putInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
                    list.add(zone.imagePath);
                    bundle.putStringArrayList(QzonePublish.PUBLISH_TO_QZONE_IMAGE_URL, list);//image_url
                    mTencent.publishToQzone(this, bundle, this);
                    return;
                } else if (params.shareVideo()) {
                    bundle.putInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHVIDEO);
                    bundle.putString(QzonePublish.PUBLISH_TO_QZONE_VIDEO_PATH, zone.videoPath);//videoPath
                    mTencent.publishToQzone(this, bundle, this);
                    return;
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
