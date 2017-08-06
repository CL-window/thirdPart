package com.cl.slack.third.share;

/**
 * Created by slack
 * on 17/7/27 下午12:51
 */

public abstract class ShareParams {

    protected enum ShareType {
        SHARE_PIC,
        SHARE_GIF,
        SHARE_VIDEO
    }

    protected ShareType mShareType = ShareType.SHARE_PIC;
    public String text;
    public String imagePath;

    public ShareParams() {
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean sharePic() {
        return mShareType == ShareType.SHARE_PIC;
    }

    public boolean shareGif() {
        return mShareType == ShareType.SHARE_GIF;
    }

    public boolean shareVideo() {
        return mShareType == ShareType.SHARE_VIDEO;
    }
}
