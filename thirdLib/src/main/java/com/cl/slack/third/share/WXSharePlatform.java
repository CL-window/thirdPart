package com.cl.slack.third.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.cl.slack.third.ThirdParty;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

/**
 * Created by slack
 * on 17/7/27 下午1:36
 */
public class WXSharePlatform extends ThirdSharePlatform {

    public final static String NAME = ThirdParty.NAME_WX;
    public final static WXSharePlatform platform = new WXSharePlatform();
    private WXSharePlatform() {
    }

    @Override
    public void shareByChild(Context context) {
        if(context == null) {
            onError("context is null!");
            return;
        }
        Intent intent = new Intent(context, WXShareActivity.class);
        context.startActivity(intent);

    }

    public static class WXShareParams extends ShareParams {

        String title;
        Bitmap thumb;
        String videoUrl;

        public int targetScene = SendMessageToWX.Req.WXSceneSession;
        // 微信好友
        public void updateSceneSession() {
            targetScene = SendMessageToWX.Req.WXSceneSession;
        }

        // 微信 朋友圈
        public void updateSceneTimeline() {
            targetScene = SendMessageToWX.Req.WXSceneTimeline;
        }

        public WXShareParams pictureParams(String path) {
            setImagePath(path);
            mShareType = ShareType.SHARE_PIC;
            return this;
        }

        public WXShareParams gifParams(String path) {
            setImagePath(path);
            mShareType = ShareType.SHARE_GIF;
            return this;
        }

        public WXShareParams videoParams(String title, String text, Bitmap thumb, String videoUrl) {
            setText(text);
            this.title = title;
            this.thumb = thumb;
            this.videoUrl = videoUrl;
            mShareType = ShareType.SHARE_VIDEO;
            return this;
        }

    }


}
