package com.cl.slack.third;

import android.support.annotation.Nullable;


import com.cl.slack.third.login.LoginPlatform;
import com.cl.slack.third.login.QQLoginPlatform;
import com.cl.slack.third.login.WBLoginPlatform;
import com.cl.slack.third.login.WXLoginPlatform;
import com.cl.slack.third.share.QQSharePlatform;
import com.cl.slack.third.share.QQZoneSharePlatform;
import com.cl.slack.third.share.ThirdSharePlatform;
import com.cl.slack.third.share.WBSharePlatform;
import com.cl.slack.third.share.WXSharePlatform;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by slack
 * on 17/7/27 下午2:10
 * share center
 */

public class ThirdParty {

    public final static String NAME_QQZONE = "QQZone";
    public final static String NAME_QQ = "QQ";
    public final static String NAME_WB = "WB";
    public final static String NAME_WX = "WX";

    private final static Map<String, ThirdSharePlatform> mSharePlatforms = new HashMap<String, ThirdSharePlatform>(){
        {
            put(NAME_WB, WBSharePlatform.platform);
            put(NAME_QQ, QQSharePlatform.platform);
            put(NAME_QQZONE, QQZoneSharePlatform.platform);
            put(NAME_WX, WXSharePlatform.platform);
        }
    };

    private final static Map<String, LoginPlatform> mLoginPlatforms = new HashMap<String, LoginPlatform>(){
        {
            put(NAME_QQ, QQLoginPlatform.platform);
            put(NAME_WB, WBLoginPlatform.platform);
            put(NAME_WX, WXLoginPlatform.platform);
        }
    };

    private final static Map<String, String> mAPPids = new HashMap<String, String>();

    @Nullable
    public static ThirdSharePlatform getSharePlatform(String name) {
        return mSharePlatforms.get(name);
    }

    @Nullable
    public static LoginPlatform getLoginPlatform(String name) {
        return mLoginPlatforms.get(name);
    }

    public static void initAppId(String name, String appId) {
        mAPPids.put(name, appId);
    }

    @Nullable
    public static String getAppId(String name) {
        return mAPPids.get(name);
    }
}
