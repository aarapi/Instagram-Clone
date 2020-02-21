package com.example.connectionframework.requestframework.sender;

import android.app.Activity;

import com.example.connectionframework.BuildConfig;
import com.example.connectionframework.R;
import com.example.connectionframework.requestframework.components.LottieDialog;
import com.example.connectionframework.requestframework.json.Serializer;
import com.example.connectionframework.requestframework.tasks.SendRequest;


public class SenderBridge {
    private Activity activity;
    private String urlConnection = BuildConfig.HOST_URL;
    private String baseReciver;
    private LottieDialog lottieDialog;

    public SenderBridge(Activity activity, String baseReciver) {
        this.activity = activity;
        this.baseReciver = baseReciver;

        lottieDialog = new LottieDialog(activity, R.raw.loading_lottie);


    }

    public void sendMessage(Request request){

        lottieDialog.show();

        SendRequest sendRequest = new SendRequest(urlConnection, baseReciver,  activity, lottieDialog);

        String jsonFormated = Serializer.toJson(request);

        sendRequest.execute(jsonFormated);
    }

}
