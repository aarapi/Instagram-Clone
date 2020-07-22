package com.instacommerce.connectionframework.requestframework.components;
import android.app.Activity;
import android.content.Context;

import com.rahman.dialog.Activity.SmartDialog;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;
import com.rahman.dialog.Utilities.SmartDialogBuilder;

public class CustomAlertBox implements SmartDialogClickListener {
    private Context mContext;
    private SmartDialogBuilder smartDialogBuilder;
    private String title, alertText;
    private boolean negativeHide;
    private Activity activity;
    private  SmartDialogClickListener listener;

    public CustomAlertBox(final Activity activity, String title, String alertText, boolean negativeHide, SmartDialogClickListener listener) {
        this.mContext = activity.getApplicationContext();
        this.title = title;
        this.alertText = alertText;
        this.negativeHide = negativeHide;
        this.activity = activity;
        this.listener = listener;
    }

    public void showAlertBox(){
        smartDialogBuilder = new SmartDialogBuilder(activity);


        activity.runOnUiThread(new Runnable() {
            public void run() {
                smartDialogBuilder.setTitle(title)
                        .setSubTitle(alertText)
                        .setCancalable(false)
                        .setNegativeButtonHide(negativeHide)
                        .setPositiveButton("OK",listener)
                        .setNegativeButton("Cancel", listener)
                        .build()
                        .show();            }
        });


    }

    @Override
    public void onClick(SmartDialog smartDialog) {
        smartDialog.dismiss();
//        Toast.makeText(mContext,"Cancel button Click",Toast.LENGTH_SHORT).show();
//        smartDialog.dismiss();
    }



}
