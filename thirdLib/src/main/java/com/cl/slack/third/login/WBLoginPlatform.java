package com.cl.slack.third.login;

import android.content.Context;
import android.content.Intent;

import com.cl.slack.third.ThirdParty;


/**
 * Created by slack
 * on 17/7/28 下午2:21
 */

public class WBLoginPlatform extends LoginPlatform {

    public final static String NAME = ThirdParty.NAME_WB;
    public final static WBLoginPlatform platform = new WBLoginPlatform();

    private WBLoginPlatform() {
    }

    @Override
    protected void loginByChild(Context context) {
        if(context != null) {
            Intent intent = new Intent(context, WBLoginActivity.class);
            context.startActivity(intent);
        } else {
            onError("context must not be null!");
        }
    }
}
