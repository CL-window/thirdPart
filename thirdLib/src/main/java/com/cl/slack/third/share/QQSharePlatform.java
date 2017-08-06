package com.cl.slack.third.share;

import android.content.Context;
import android.content.Intent;

import com.cl.slack.third.ThirdParty;

/**
 * Created by slack
 * on 17/7/27 下午1:36
 */
public class QQSharePlatform extends ThirdSharePlatform {

    public final static String NAME = ThirdParty.NAME_QQ;
    public final static QQSharePlatform platform = new QQSharePlatform();
    private QQSharePlatform() {
    }

    @Override
    public void shareByChild(Context context) {
        if(context == null) {
            onError("context is null!");
            return;
        }
        Intent intent = new Intent(context, QQShareActivity.class);
        context.startActivity(intent);

    }

    public static class QQShareParams extends ShareParams {

        String actionUrl;
        String title;
        String thumbUrl;

        public QQShareParams pictureParams(String path) {
            setImagePath(path);
            mShareType = ShareType.SHARE_PIC;
            return this;
        }

        public QQShareParams videoParams(String title, String actionUrl, String content, String thumbUrl) {
            setText(content);
            this.title = title;
            this.actionUrl = actionUrl;
            this.thumbUrl = thumbUrl;
            mShareType = ShareType.SHARE_VIDEO;
            return this;
        }

    }


}
