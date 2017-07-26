package com.wapchief.jpushim.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by wapchief on 2017/7/25.
 */

public class MyProgressDialog {

    public ProgressDialog progressDialog;
    public Context context;

    public MyProgressDialog(Context context, String msg) {
        this.context = context;
    }

    /*自定义消息的加载进度条*/
    public void showProgressDialog(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        try {
            progressDialog.show();
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 隐藏正在加载的进度条
     *
     */
    public void dismissProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing() == true) {
            progressDialog.dismiss();
        }
    }
}
