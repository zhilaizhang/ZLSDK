package com.zlzhang.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

/**
 * Created by zhilaizhang on 17/7/8.
 */

public class ToastUtil {
    /**
     * 简单的toast
     * @param activity
     * @param s
     */
    public static void toast(final Activity activity, final String s) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 包含背景和字体颜色和大小的toast
     * @param activity
     * @param s
     * @param backGroundColor
     * @param textColor
     * @param textSize
     */
    public static void toast(final Activity activity, final String s, final int backGroundColor, final int textColor, final int textSize) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(activity, s, Toast.LENGTH_SHORT);
                LinearLayout layout = (LinearLayout) toast.getView();
                layout.setBackgroundColor(backGroundColor);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextColor(textColor);
                v.setTextSize(textSize);
                toast.show();
            }
        });
    }

    /**
     * 包含字体颜色和大小的
     * @param activity
     * @param s
     * @param textColor
     * @param textSize
     */
    public static void toast(final Activity activity, final String s, final int textColor, final int textSize) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(activity, s, Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextColor(textColor);
                v.setTextSize(textSize);
                toast.show();
            }
        });
    }

    private static final SimpleDateFormat mDateFormater =
            new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss.SSS");

    private static final SimpleDateFormat mDateFormaterToday =
            new SimpleDateFormat("HH:mm:ss.SSS");

    private static String getTimeStr() {
        return mDateFormaterToday.format(System.currentTimeMillis());
    }

    public static void showAlert(Context context, String title, String message, boolean isShowCancelButton
            , boolean isOutCancel, final OnDialogClickListener onDialogClickListener) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        onDialogClickListener.onPositiveClick();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        onDialogClickListener.onNegativeClick();
                        break;
                }
            }
        };
        try {
            AlertDialog alerDialog;
            if (isShowCancelButton) {
                alerDialog = new AlertDialog.Builder(context)
                        .setNegativeButton(android.R.string.no, dialogClickListener)
                        .setPositiveButton(android.R.string.yes, dialogClickListener).create();
            } else {
                alerDialog = new AlertDialog.Builder(context)
                        .setPositiveButton(android.R.string.yes, dialogClickListener).create();
            }

            if (!TextUtil.isTextEmpty(title)) {
                alerDialog.setTitle(title);
            }
            if (!TextUtil.isTextEmpty(message)) {
                alerDialog.setMessage(message);
            }
            alerDialog.setCancelable(isOutCancel);
            alerDialog.show();
        } catch (Exception ignored) {

        }
    }

    public static void showAlertList(Context context, String title, final String[] messages, final OnDialogListClickListener onDialogListClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(messages, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDialogListClickListener.onSelected(which, messages[which]);
            }
        });
        builder.create();
        builder.show();
    }

    public interface OnDialogClickListener {
        void onPositiveClick();

        void onNegativeClick();
    }

    public interface OnDialogListClickListener {
        void onSelected(int position, String message);
    }
}
