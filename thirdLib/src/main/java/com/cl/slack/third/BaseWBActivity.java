package com.cl.slack.third;

import android.os.Bundle;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

/**
 * Created by slack
 * on 17/7/27 上午11:54
 */

public abstract class BaseWBActivity extends BaseOldActivity{

    private static final String REDIRECT_URL = "http://www.qumeihuiying.com";

    private static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WbSdk.install(this, new AuthInfo(this,
                ThirdParty.getAppId(ThirdParty.NAME_WB), REDIRECT_URL, SCOPE));
    }
}
