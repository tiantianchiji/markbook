package com.rounima.markbook.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

public class AlertDialogUtil {
    /**安卓底部消息提示*/
    public static void showToast(Context ctx,String info,int length){
        Toast.makeText(ctx,info,length).show();
    }
    /**普通弹出框*/
    public static void commonAlertDialog(Context ctx, String info, DialogInterface.OnDismissListener ondismiss){
        new AlertDialog.Builder(ctx)
                .setMessage(info)
                .setPositiveButton("确定",null)
                .setOnDismissListener(ondismiss)
                .show();
    }
    /**自定义View弹出框*/
    public static AlertDialog customAlertDialog(Context ctx,View view){
        return new AlertDialog.Builder(ctx)
                .setView(view)
                .setCancelable(false)
                .show();
    }
    /**只带一个确认按钮,并且不能取消*/
    public static void yesButtonWithoutCancel(Context ctx,String info,DialogInterface.OnClickListener click){
        new AlertDialog.Builder(ctx)
                .setMessage(info)
                .setPositiveButton("确定",click)
                .show();
    }
    /**带有取消和确认按钮的弹出框*/
    public static AlertDialog alertDialogWithCancelAndYes(Context ctx,String title,String info,boolean cancalable,String cancelText,String yesText,DialogInterface.OnClickListener cancel,DialogInterface.OnClickListener yes){
        return new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(info)
                .setCancelable(cancalable)
                .setPositiveButton(yesText,yes)
                .setNegativeButton(cancelText,cancel)
                .show();
    }
    /**展示信息dialog*/
    public static AlertDialog showDialog(Context ctx,String title,String info,boolean cancalable){
        return new AlertDialog.Builder(ctx).setTitle(title)
                .setMessage(info)
                .setCancelable(cancalable)
                .show();
    }
}
