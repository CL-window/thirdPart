package com.cl.slack.third.share;

import android.content.Context;
import android.support.annotation.Nullable;

import com.cl.slack.third.Platform;


/**
 * Created by slack
 * on 17/7/27 下午12:55
 */

public abstract class ThirdSharePlatform extends Platform {

    private ShareParams mShareParams;

    @Nullable
    public ShareParams getShareParams() {
        return mShareParams;
    }

    public void share(Context context, ShareParams params) {
        mShareParams = params;
        shareByChild(context);
    }

    protected abstract void shareByChild(Context context);

    public void onComplete(String... params) {
        mShareParams = null;
        super.onComplete(params);
    }

    public void onError(String msg) {
        mShareParams = null;
        super.onError(msg);
    }

    public void onCancel() {
        mShareParams = null;
        super.onCancel();
    }
}
