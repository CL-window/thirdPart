package com.cl.slack.third;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.widget.Toast;


/**
 * qq weixin 最低支持
 * Created by slack
 * on 17/3/24 下午1:39.
 */

public abstract class BaseOldActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixin_entry);
    }

    protected void showCenterToast(final @StringRes int id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseOldActivity.this, id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void showCenterToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseOldActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void finishWithSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    protected void finishWithError() {
        setResult(RESULT_CANCELED);
        finish();
    }

}
