package com.example.connectionframework.requestframework.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.connectionframework.requestframework.components.LottieDialog;
import com.example.connectionframework.requestframework.constants.Constants;
import com.example.connectionframework.requestframework.httpconnection.HttpConnectionException;
import com.example.connectionframework.requestframework.httpconnection.HttpUtil;
import com.example.connectionframework.requestframework.receiver.ReceiverBridge;


public class SendRequest extends AsyncTask<String, Integer, String> {
    private String request;
    private Activity baseActivity;
    private ReceiverBridge receiverBridge;
    private String messageServiceUrl;
    private String baseReceiver;
    private LottieDialog lottieDialog;

    public SendRequest(String messageServiceUrl, String baseReceiver,
                       Activity baseActivity, LottieDialog lottieDialog) {
        this.messageServiceUrl = messageServiceUrl;
        this.baseActivity = baseActivity;
        receiverBridge = new ReceiverBridge(baseActivity, baseReceiver, lottieDialog);
        this.baseReceiver = baseReceiver;
        this.lottieDialog = lottieDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... sendMessage) {
        String _rtn = "";
        try {
            this.request = sendMessage[0];
            _rtn = getResponse(this.request);

        }catch (HttpConnectionException e){
            if (e.getMessage() != null && e.getMessage().equals(Constants.Application.CONNECTION_TIMED_OUT_ERROR_MESSAGE)){
                _rtn = Constants.Application.CONNECTION_TIMED_OUT_ERROR_MESSAGE;
            }else if (e.getMessage() != null && e.getMessage().equals(Constants.Application.CONNECTION_TIMED_OUT_ERROR_MESSAGE)){
                _rtn = Constants.Application.CONNECTION_OTHER_EXCEPTION;
            }else if (e.getMessage() != null && e.getMessage().equals(Constants.Application.CONNECTION_OTHER_EXCEPTION)){
                _rtn = Constants.Application.CONNECTION_OTHER_EXCEPTION;
            }
        }
        if(!isCancelled()) {
            if (receiverBridge != null) {
                receiverBridge.responseReceived(_rtn);
            }
        }
        return _rtn;
    }

    @Override
    protected void onPostExecute(String  result) {
        super.onPostExecute(result);
    }


    private String getResponse(String jsonFormatedRequest) {


        String jsonResponse = HttpUtil.getResponse(this.messageServiceUrl,
                jsonFormatedRequest);


        return jsonResponse;
    }


}
