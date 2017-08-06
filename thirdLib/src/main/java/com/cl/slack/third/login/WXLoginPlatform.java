package com.cl.slack.third.login;

import android.content.Context;
import android.content.Intent;

import com.cl.slack.third.ThirdParty;
import com.cl.slack.third.wxapi.WXEntryActivity;

/**
 * Created by slack
 * on 17/7/28 下午2:21
 */

public class WXLoginPlatform extends LoginPlatform {

    public final static String NAME = ThirdParty.NAME_WX;
    public final static WXLoginPlatform platform = new WXLoginPlatform();

    private WXLoginPlatform() {
    }

    @Override
    protected void loginByChild(Context context) {
        if(context != null) {
            Intent intent = new Intent(context, WXEntryActivity.class);
            context.startActivity(intent);
        } else {
            onError("context must not be null!");
        }
    }
}
