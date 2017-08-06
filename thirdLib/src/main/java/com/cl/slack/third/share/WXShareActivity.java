package com.cl.slack.third.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.cl.slack.third.BaseWXActivity;
import com.cl.slack.third.ThirdParty;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXEmojiObject;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by slack
 * on 17/7/27 上午11:54
 * doc: https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340&token=&lang=zh_CN
 * 微信的分享拿不到回调
 */

public class WXShareActivity extends BaseWXActivity {

    private static final int THUMB_SIZE = 80;
    private WXSharePlatform mPlatform;
    private boolean mInResumeAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThirdSharePlatform p = ThirdParty.getSharePlatform(WXSharePlatform.NAME);
        if(p instanceof WXSharePlatform) {
            mPlatform = (WXSharePlatform) p;
            sendMultiMessage();
        } else {
            finishWithError();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mInResumeAgain) {
            mInResumeAgain = true;
        } else {
            finishWithError();
        }
    }

    private void sendMultiMessage() {
        if(mPlatform != null) {
            WXSharePlatform.WXShareParams params = (WXSharePlatform.WXShareParams) mPlatform.getShareParams();
            if(params != null) {
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                if(params.sharePic()) {
                    WXImageObject imgObj = new WXImageObject();
                    imgObj.setImagePath(params.imagePath);
                    WXMediaMessage msg = new WXMediaMessage();
                    msg.mediaObject = imgObj;
                    msg.setThumbImage(decodeFile(params.imagePath));

                    req.transaction = buildTransaction("img");
                    req.message = msg;
                    req.scene = params.targetScene;
                    sendReq(req);
                    return;
                } else if(params.shareGif()) {
                    WXEmojiObject emoji = new WXEmojiObject();
                    emoji.emojiPath = params.imagePath;

                    WXMediaMessage msg = new WXMediaMessage(emoji);
                    msg.setThumbImage(decodeFile(params.imagePath)); // gif 一定要有thumb
                    req.transaction = buildTransaction("emoji");
                    req.message = msg;
                    req.scene = params.targetScene;
                    sendReq(req);
                    return;
                } else if(params.shareVideo()) {
                    WXVideoObject video = new WXVideoObject();
                    video.videoUrl = params.videoUrl;

                    WXMediaMessage msg = new WXMediaMessage(video);
                    msg.title = params.title;
                    msg.description = params.text;
                    if(params.thumb != null) {
                        msg.setThumbImage(compressBitmap(params.thumb));
                    }

                    req.transaction = buildTransaction("video");
                    req.message = msg;
                    req.scene = params.targetScene;
                    sendReq(req);
                    return;
                }
            }
        }
        finishWithError();
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private Bitmap decodeFile(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return compressBitmap(bitmap);
    }

    // bitmap < 32Kb
    private Bitmap compressBitmap(Bitmap bitmap) {
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        return thumbBmp;
    }

    private byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (needRecycle) {
            bmp.recycle();
        }

        return result;
    }

}
