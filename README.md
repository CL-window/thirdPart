# thirdPart:
负责
* 第三方登录，比如微信 QQ 微博的登录
* 第三方分享 微信  QQ 微博 ...

暂时知识这三个平台的... 
分享支持 图片，动图，视频（部分平台需要视频网络的url)
platform : 第三方平台的封装

share.ShareParams 第三方分享时参数封装

### beofre use
*  [QQ jar download](http://wiki.open.qq.com/wiki/mobile/SDK%E4%B8%8B%E8%BD%BD) 我使用的 Android_SDK_V3.2.1, 添加到项目libs里
*  微信：module 的 build.gradle dependencies add `// 微信 不包含统计
    compile 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:1.0.2'`
*  [微博SDK jar](https://github.com/sinaweibosdk/weibo_android_sdk)
    * 修改工程的主模块下面修改build.gradle文件为`allprojects {
    repositories {
        jcenter()
        mavenCentral()
        maven { url "https://dl.bintray.com/thelasterstar/maven/" }
    }
}`
    * module 的 build.gradle dependencies add `compile 'com.sina.weibo.sdk:core:4.1.0:openDefaultRelease@aar'`

* 初始化第三方申请的APPId, ` ThirdParty.initAppId(ThirdParty.NAME_QQ, "QQ App Id");
        ThirdParty.initAppId(ThirdParty.NAME_WB, "weibo App Id");
        ThirdParty.initAppId(ThirdParty.NAME_WX, "weixin App Id");`

### how to use
##### login / bind:
```
LoginPlatform platform = ThirdParty.getLoginPlatform(QQLoginPlatform.NAME);
    if(platform != null) {
        platform.setOperateListener(new QQLoginListener());
        platform.login(this, LoginPlatform.LoginState.STATE_LOGIN);
    }
```
##### share:
``` 
QQSharePlatform.QQShareParams params = new QQSharePlatform.QQShareParams();
        switch ("shareType") {
            case "pic":
            case "gif":
                params.pictureParams("file local path like file.getAbsolutePath()");
                break;

            case "video":
                params.videoParams("share title", "share title + video web url", "share message", "thumb url");
                break;
        }

ThirdSharePlatform platform = ThirdParty.getSharePlatform(QQSharePlatform.NAME);
if(platform != null) {
    platform.setOperateListener(new ShareListener());
    platform.share(this, params);
}
```

具体的伪代码
```
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cl.slack.third.OperateListener;
import com.cl.slack.third.ThirdParty;
import com.cl.slack.third.login.LoginPlatform;
import com.cl.slack.third.login.QQLoginPlatform;
import com.cl.slack.third.login.WBLoginPlatform;
import com.cl.slack.third.login.WXLoginPlatform;
import com.cl.slack.third.share.QQSharePlatform;
import com.cl.slack.third.share.QQZoneSharePlatform;
import com.cl.slack.third.share.ShareParams;
import com.cl.slack.third.share.ThirdSharePlatform;
import com.cl.slack.third.share.WBSharePlatform;
import com.cl.slack.third.share.WXSharePlatform;

/**
 * created by slack
 * on 17/8/6 下午5:08
 * only show how to use
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAppID();
    }

    private void initAppID() {
        ThirdParty.initAppId(ThirdParty.NAME_QQ, "QQ App Id");
        ThirdParty.initAppId(ThirdParty.NAME_WB, "weibo App Id");
        ThirdParty.initAppId(ThirdParty.NAME_WX, "weixin App Id");
    }

    public void loginQQ(View view) {
        LoginPlatform platform = ThirdParty.getLoginPlatform(QQLoginPlatform.NAME);
        if(platform != null) {
            platform.setOperateListener(new QQLoginListener());
            platform.login(this, LoginPlatform.LoginState.STATE_LOGIN);
        }
    }

    public void loginWX(View view) {
        LoginPlatform platform = ThirdParty.getLoginPlatform(WXLoginPlatform.NAME);
        if(platform != null) {
//            platform.setOperateListener(new WXLoginListener());
            platform.login(this, LoginPlatform.LoginState.STATE_LOGIN);
        }
    }

    public void loginWB(View view) {
        LoginPlatform platform = ThirdParty.getLoginPlatform(WBLoginPlatform.NAME);
        if(platform != null) {
//            platform.setOperateListener(new WBLoginListener());
            platform.login(this, LoginPlatform.LoginState.STATE_LOGIN);
        }
    }

    /**
     * 分享QQ
     * 图片，动图 ： 本地文件
     * 视频      ：需要自己上传视频, 提供视频的网络地址 ,还需要 缩略图的网络地址
     */
    public void shareQQ(View view) {
        QQSharePlatform.QQShareParams params = new QQSharePlatform.QQShareParams();
        switch ("shareType") {
            case "pic":
            case "gif":
                params.pictureParams("file local path like file.getAbsolutePath()");
                break;

            case "video":
                params.videoParams("share title", "share title + video web url", "share message", "thumb url");
                break;
        }

        ThirdSharePlatform platform = ThirdParty.getSharePlatform(QQSharePlatform.NAME);
        share(platform, params);
    }

    /**
     * 分享QQZone
     * 图片，动图,视频 ： 本地文件
     */
    public void shareQQZone(View view) {
        QQZoneSharePlatform.QQZoneShareParams params = new QQZoneSharePlatform.QQZoneShareParams();
        switch ("shareType") {
            case "pic":
            case "gif":
                params.pictureParams("share title", "file local path like file.getAbsolutePath()");
                break;

            case "video":
                params.videoParams("share title", "file local path like file.getAbsolutePath()");
                break;
        }

        ThirdSharePlatform platform = ThirdParty.getSharePlatform(QQZoneSharePlatform.NAME);
        share(platform, params);
    }

    /**
     * 分享微信
     * 图片，动图 ： 本地文件
     * 视频      ：如果文件小与19M，直接走微信的本地分享, 否则需要自己上传视频, 提供视频的网络地址，提供缩略图的 Bitmap
     */
    public void shareWX(View view) {
        WXSharePlatform.WXShareParams params = new WXSharePlatform.WXShareParams();
        params.updateSceneSession();
        switch ("shareType") {
            case "pic":
                params.pictureParams("file local path like file.getAbsolutePath()");
                break;
            case "gif":
                params.gifParams("file local path like file.getAbsolutePath()");
                break;
            case "video":
                if("video size < 19M") {
                    shareVideoToWechatFromLocal();
                    return;
                }
                params.videoParams("share title", "share message", "thumb bitmap", "video web url");
                break;
        }

        ThirdSharePlatform platform = ThirdParty.getSharePlatform(WXSharePlatform.NAME);
        share(platform, params);
    }

    /**
     * 分享微信朋友圈
     * 图片      ： 本地文件
     * 视频      ：需要自己上传视频, 提供视频的网络地址，提供缩略图的 Bitmap
     */
    public void shareWXFriends(View view) {
        WXSharePlatform.WXShareParams params = new WXSharePlatform.WXShareParams();
        params.updateSceneTimeline();
        switch ("shareType") {
            case "pic":
                params.pictureParams("file local path like file.getAbsolutePath()");
                break;
            case "video":
                params.videoParams("share title", "share message", "thumb bitmap", "video web url");
                break;
        }

        ThirdSharePlatform platform = ThirdParty.getSharePlatform(WXSharePlatform.NAME);
        share(platform, params);
    }

    /**
     * 直接分享 视频 到 微信
     */
    private void shareVideoToWechatFromLocal() {
        Uri uri = null; // file url
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setComponent(new ComponentName(
                "com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(shareIntent);
    }


    /**
     * 分享微博
     * 图片，动图 ： 本地文件
     * 视频      ：需要自己上传视频, 提供视频的网络地址
     */
    public void shareWB(View view) {
        WBSharePlatform.WBShareParams params = new WBSharePlatform.WBShareParams();
        switch ("shareType") {
            case "pic":
            case "gif":
                params.pictureParams("share title", "file local path like file.getAbsolutePath()");
                break;

            case "video":
                params.videoParams("share title + video web url");
                break;
        }
        ThirdSharePlatform platform = ThirdParty.getSharePlatform(WBSharePlatform.NAME);
        share(platform, params);
    }

    private void share(ThirdSharePlatform platform, ShareParams params) {
        if(platform != null) {
            platform.setOperateListener(new ShareListener());
            platform.share(this, params);
        }
    }

    private class QQLoginListener implements OperateListener {

        QQLoginListener() {

        }

        @Override
        public void onComplete(String... params) {
            LoginPlatform platform = ThirdParty.getLoginPlatform(WXLoginPlatform.NAME);
            if(platform != null) {
                switch (platform.mLoginState) {
                    case STATE_LOGIN:
                        //
                        break;
                    case STATE_BIND:
                        //
                        break;
                }
            }
        }

        @Override
        public void onError(String msg) {
            //
        }

        @Override
        public void onCancel() {
            //
        }
    }

    private class ShareListener implements OperateListener {

        ShareListener() {

        }

        @Override
        public void onComplete(String... params) {
            //
        }

        @Override
        public void onError(String msg) {
            //
        }

        @Override
        public void onCancel() {
            //
        }
    }
}

```