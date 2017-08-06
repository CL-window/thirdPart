package com.cl.slack.third;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * weixin needs   extends Activity
 * Created by slack
 * on 17/3/23 上午11:52.
 */

public abstract class BaseWXActivity extends BaseOldActivity implements IWXAPIEventHandler {

    private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * 注册到微信,使微信终端能响应程序
         */
        String weixinId = ThirdParty.getAppId(ThirdParty.NAME_WX);
        mIWXAPI = WXAPIFactory.createWXAPI(this, weixinId, true);
        mIWXAPI.registerApp(weixinId);

        super.onCreate(savedInstanceState);

        mIWXAPI.handleIntent(getIntent(),this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mIWXAPI.handleIntent(intent,this);
    }

    protected void sendReq(SendMessageToWX.Req req) {
        mIWXAPI.sendReq(req);
    }
    protected void openWeixin(){
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "none";
        mIWXAPI.sendReq(req);
    }

    protected boolean checkWXSupportPay() {
        return mIWXAPI.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    protected boolean openWeixinPay(BaseReq req) {
        return mIWXAPI.sendReq(req);
    }

    /**
     * 当微信发送请求到你的应用，将通过IWXAPIEventHandler接口的onReq方法进行回调
     */
    @Override
    public void onReq(BaseReq req) {
        Log.i("slack","onReq...");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.i("slack","onResp...");
    }
}
