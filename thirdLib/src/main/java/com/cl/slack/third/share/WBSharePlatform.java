package com.cl.slack.third.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.cl.slack.third.ThirdParty;


/**
 * Created by slack
 * on 17/7/27 下午1:36
 */
public class WBSharePlatform extends ThirdSharePlatform {

    public final static String NAME = ThirdParty.NAME_WB;
    public final static WBSharePlatform platform = new WBSharePlatform();
    private WBSharePlatform() {
    }

    @Override
    public void shareByChild(Context context) {
        if(context == null) {
            onError("context is null!");
            return;
        }
        Intent intent = new Intent(context, WBShareActivity.class);
        context.startActivity(intent);

    }

    public static class WBShareParams extends ShareParams {

        Bitmap bitmap;

        public WBShareParams pictureParams(String title, Bitmap b) {
            setText(title);
            bitmap = b;
            mShareType = ShareType.SHARE_PIC;
            return this;
        }

        public WBShareParams pictureParams(String title, String path) {
            setText(title);
            setImagePath(path);
            mShareType = ShareType.SHARE_PIC;
            return this;
        }

        public WBShareParams videoParams(String title) {
            mShareType = ShareType.SHARE_VIDEO;
            setText(title);
            return this;
        }

    }


}
