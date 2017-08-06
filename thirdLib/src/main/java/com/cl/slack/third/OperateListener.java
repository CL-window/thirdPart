package com.cl.slack.third;

/**
 * Created by slack
 * on 17/7/27 下午1:24
 */

public interface OperateListener {
    void onComplete(String... params);

    void onError(String msg);

    void onCancel();
}
