package com.cl.slack.third.share;

import android.content.Context;
import android.content.Intent;

import com.cl.slack.third.ThirdParty;

/**
 * Created by slack
 * on 17/7/27 下午1:36
 */
public class QQZoneSharePlatform extends ThirdSharePlatform {

    public final static String NAME = ThirdParty.NAME_QQZONE;
    public final static QQZoneSharePlatform platform = new QQZoneSharePlatform();
    private QQZoneSharePlatform() {
    }

    @Override
    public void shareByChild(Context context) {
        if(context == null) {
            onError("context is null!");
            return;
        }
        Intent intent = new Intent(context, QQZoneShareActivity.class);
        context.startActivity(intent);

    }

    public static class QQZoneShareParams extends ShareParams {

        String videoPath;

        public QQZoneShareParams pictureParams(String text, String path) {
            setText(text);
            setImagePath(path);
            mShareType = ShareType.SHARE_PIC;
            return this;
        }

        public QQZoneShareParams videoParams(String text, String videoPath) {
            setText(text);
            this.videoPath = videoPath;
            mShareType = ShareType.SHARE_VIDEO;
            return this;
        }

    }


}
