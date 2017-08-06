package com.cl.slack.third;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;


import java.lang.ref.WeakReference;

/**
 * Created by slack
 * on 17/7/27 下午12:55
 */

public abstract class Platform {

    private WeakReference<Context> mContext;

    protected OperateListener mOperateListener;
    public void setOperateListener(OperateListener l) {
        mOperateListener = l;
    }

    public void updateContext(Context context) {
        clear();
        mContext = new WeakReference<Context>(context);
    }

    public void clear() {
        if(mContext != null) {
            mContext.clear();
        }
    }

    @Nullable
    public Context getContext() {
        if(mContext == null) {
            return null;
        }
        return mContext.get();
    }

    public void onComplete(String... params) {
        Log.i("slack", "onComplete...");
        if(mOperateListener != null) {
            mOperateListener.onComplete(params);
        }
    }

    public void onError(String msg) {
        Log.i("slack", "onError...");
        if(mOperateListener != null) {
            mOperateListener.onError(msg);
        }
    }

    public void onCancel() {
        Log.i("slack", "onCancel...");
        if(mOperateListener != null) {
            mOperateListener.onCancel();
        }
    }
}
