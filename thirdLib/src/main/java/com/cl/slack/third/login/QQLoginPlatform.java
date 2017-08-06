package com.cl.slack.third.login;

import android.content.Context;
import android.content.Intent;

import com.cl.slack.third.ThirdParty;


/**
 * Created by slack
 * on 17/7/28 下午2:21
 */

public class QQLoginPlatform extends LoginPlatform {

    public final static String NAME = ThirdParty.NAME_QQ;
    public final static QQLoginPlatform platform = new QQLoginPlatform();

    private QQLoginPlatform() {
    }

    @Override
    protected void loginByChild(Context context) {
        if(context != null) {
            Intent intent = new Intent(context, QQLoginActivity.class);
            context.startActivity(intent);
        } else {
            onError("context must not be null!");
        }
    }
}
