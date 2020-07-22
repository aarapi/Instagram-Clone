package com.instacommerce.connectionframework.requestframework.sender;

import com.instacommerce.connectionframework.BuildConfig;
import com.instacommerce.connectionframework.requestframework.components.LottieDialog;
import com.instacommerce.connectionframework.requestframework.json.Serializer;
import com.instacommerce.connectionframework.requestframework.receiver.RequestReceived;
import com.instacommerce.connectionframework.requestframework.tasks.SendRequest;


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
