package com.wapchief.jpushim.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.wapchief.jpushim.MainActivity;
import com.wapchief.jpushim.activity.AddFriendsActivity;
import com.wapchief.jpushim.framework.utils.UIUtils;

/**
 * Created by wapchief on 2017/7/26.
 */

public class MyAlertDialog {
    private Context context;
    private String[] strings;
    AlertDialog dialog;
    DialogInterface.OnClickListener onClick;
    public MyAlertDialog(Context context, String[] strings,DialogInterface.OnClickListener onClick) {
        this.context = context;
        this.strings = strings;
        this.onClick = onClick;
    }

    public void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(strings, onClick);
        dialog = builder.create();
        dialog.show();

    }

    public void dialogSize(int width,int whight,int x,int y){
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setGravity(Gravity.RIGHT | Gravity.TOP);
        params.width = UIUtils.dip2px(context, width);
        params.y = UIUtils.dip2px(context, y);
        dialog.getWindow().setAttributes(params);
    }


}
