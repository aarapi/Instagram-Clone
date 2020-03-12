package com.example.connectionframework.requestframework.receiver;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.connectionframework.requestframework.components.CustomAlertBox;
import com.example.connectionframework.requestframework.components.LottieDialog;
import com.example.connectionframework.requestframework.constants.Constants;
import com.example.connectionframework.requestframework.constants.MessagingFrameworkConstant;
import com.example.connectionframework.requestframework.json.JsonWrapper;
import com.example.connectionframework.requestframework.sender.Message;
import com.rahman.dialog.Activity.SmartDialog;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReceiverBridge {
    private Activity baseActivity;
    private String baseReceiver;
    private LottieDialog lottieDialog;
    private String response;
    private SmartDialogClickListener listener;

    public ReceiverBridge(Activity baseActivity, String basereceiver, LottieDialog lottieDialog) {
        this.baseActivity = baseActivity;
        this.baseReceiver = basereceiver;
        this.lottieDialog = lottieDialog;
    }

    public void responseReceived(String response){
        this.response = response;

        lottieDialog.dismiss();

        Message message = returnMessage(response);

        Log.e("Response", response);

        int status = message.getStatusCode();


        switch (status){
            case MessagingFrameworkConstant.STATUS_CODES.Inform:
                break;
            case MessagingFrameworkConstant.STATUS_CODES.Success:
                requestAppearSuccesfully(message);
                break;
            case MessagingFrameworkConstant.STATUS_CODES.WarningWithoutAlert:
                break;
            case MessagingFrameworkConstant.STATUS_CODES.Error:
            case MessagingFrameworkConstant.STATUS_CODES.Warning:
            case MessagingFrameworkConstant.STATUS_CODES.ConnectionTimedOut:
                  requestAppearNotSucessfully(message);
                break;
            case MessagingFrameworkConstant.STATUS_CODES.ConnectionFailed:
                requestFailed(message);
                break;






        }

    }

    private void requestAppearSuccesfully(Message message){
        Intent intent = new Intent(baseReceiver + message.getAction());

        intent.putExtra("action", message.getAction());
        intent.putExtra("data", response);

        baseActivity.sendBroadcast(intent);

    }

    private void requestAppearNotSucessfully(Message message){

       String reason = (String) message.getData().get(0);

       listener = new SmartDialogClickListener() {
           @Override
           public void onClick(SmartDialog smartDialog) {
             smartDialog.dismiss();
           }
       };

        CustomAlertBox customAlertBox = new CustomAlertBox(baseActivity,"Warning",
                reason, true, listener);

        customAlertBox.showAlertBox();
    }

    private void requestFailed(Message message){
        String reason = (String) message.getData().get(0);

        listener = new SmartDialogClickListener() {
            @Override
            public void onClick(SmartDialog smartDialog) {
                smartDialog.dismiss();
            }
        };

        CustomAlertBox customAlertBox = new CustomAlertBox(baseActivity,"Warning",
                reason, true, listener);

        customAlertBox.showAlertBox();

    }

    private Message returnMessage(String response){
        Message message = new Message();
        List<Object> data = new ArrayList<>();

        if(response.equals(Constants.Application.CONNECTION_TIMED_OUT_ERROR_MESSAGE)){

            message.setStatusCode(MessagingFrameworkConstant.STATUS_CODES.ConnectionTimedOut);
            data.add(response);
            message.setData(data);

        }else if(response.equals(Constants.Application.CONNECTION_OTHER_EXCEPTION)){

            message.setStatusCode(MessagingFrameworkConstant.STATUS_CODES.ConnectionFailed);
            data.add(response);
            message.setData(data);
        }
        else {
            message = JsonWrapper.getobject(response);
        }

        return message;
    }

}
