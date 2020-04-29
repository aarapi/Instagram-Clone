package com.example.connectionframework.requestframework.sender;

import android.app.Activity;

import com.example.connectionframework.BuildConfig;
import com.example.connectionframework.R;
import com.example.connectionframework.requestframework.components.LottieDialog;
import com.example.connectionframework.requestframework.json.Serializer;
import com.example.connectionframework.requestframework.receiver.RequestReceived;
import com.example.connectionframework.requestframework.tasks.SendRequest;


public class SenderBridge {
    private RequestReceived requestReceived;
    private String urlConnection = BuildConfig.HOST_URL;
    private String baseReciver;
    private LottieDialog lottieDialog;

    public SenderBridge(RequestReceived requestReceived) {
        this.requestReceived = requestReceived;

//        lottieDialog = new LottieDialog(requestReceived, R.raw.loading_lottie);


    }

    public void sendMessage(Request request){

//        lottieDialog.show();
        SendRequest sendRequest = new SendRequest(requestReceived, lottieDialog);

        String jsonFormated = Serializer.toJson(request);

        sendRequest.execute(jsonFormated);
    }

}
