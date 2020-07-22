package com.instacommerce.connectionframework.requestframework.receiver;

import android.util.Log;

import com.instacommerce.connectionframework.requestframework.components.LottieDialog;
import com.instacommerce.connectionframework.requestframework.constants.Constants;
import com.instacommerce.connectionframework.requestframework.constants.MessagingFrameworkConstant;
import com.instacommerce.connectionframework.requestframework.json.JsonWrapper;
import com.instacommerce.connectionframework.requestframework.sender.Message;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;

import java.util.ArrayList;
import java.util.List;

public class ReceiverBridge {
    private RequestReceived requestReceived;
//    private LottieDialog lottieDialog;
    private String response;
    private SmartDialogClickListener listener;

    public ReceiverBridge(RequestReceived requestReceived, LottieDialog lottieDialog) {
        this.requestReceived = requestReceived;
//        this.lottieDialog = lottieDialog;
    }

    public void responseReceived(String response){
        this.response = response;

//        lottieDialog.dismiss();

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
            case MessagingFrameworkConstant.STATUS_CODES.Error:
            case MessagingFrameworkConstant.STATUS_CODES.Warning:
            case MessagingFrameworkConstant.STATUS_CODES.ConnectionTimedOut:
            case MessagingFrameworkConstant.STATUS_CODES.ConnectionFailed:
                errorMessageReceived(message);
                errorMessageReceived(message, status);
                break;






        }

    }

    private void requestAppearSuccesfully(Message message){
        requestReceived.onRequestReceived(message.getAction(), message.getData());
    }
    private void errorMessageReceived(Message message){
        requestReceived.onErrorReceived(message.getAction(), message.getData());
    }

    private void errorMessageReceived(Message message, int status) {
        requestReceived.onErrorReceived(message.getAction(), message.getData(), status);
    }


//    private void requestAppearNotSucessfully(Message message){
//
//       String reason = (String) message.getData().get(0);
//
//       listener = new SmartDialogClickListener() {
//           @Override
//           public void onClick(SmartDialog smartDialog) {
//             smartDialog.dismiss();
//           }
//       };
//
//        CustomAlertBox customAlertBox = new CustomAlertBox(baseActivity,"Warning",
//                reason, true, listener);
//
//        customAlertBox.showAlertBox();
//    }
//
//    private void requestFailed(Message message){
//        String reason = (String) message.getData().get(0);
//
//        listener = new SmartDialogClickListener() {
//            @Override
//            public void onClick(SmartDialog smartDialog) {
//                smartDialog.dismiss();
//            }
//        };
//
//        CustomAlertBox customAlertBox = new CustomAlertBox(baseActivity,"Warning",
//                reason, true, listener);
//
//        customAlertBox.showAlertBox();
//
//    }

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
