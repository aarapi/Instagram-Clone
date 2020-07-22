package com.instacommerce.connectionframework.requestframework.tasks;

import android.os.AsyncTask;

import com.instacommerce.connectionframework.BuildConfig;
import com.instacommerce.connectionframework.requestframework.components.LottieDialog;
import com.instacommerce.connectionframework.requestframework.constants.Constants;
import com.instacommerce.connectionframework.requestframework.httpconnection.HttpConnectionException;
import com.instacommerce.connectionframework.requestframework.httpconnection.HttpUtil;
import com.instacommerce.connectionframework.requestframework.receiver.ReceiverBridge;
import com.instacommerce.connectionframework.requestframework.receiver.RequestReceived;


public class SendRequest extends AsyncTask<String, Integer, String> {
    private String request;
    private ReceiverBridge receiverBridge;
    private String messageServiceUrl = BuildConfig.HOST_URL;

    public SendRequest(RequestReceived requestReceived, LottieDialog lottieDialog) {
        receiverBridge = new ReceiverBridge(requestReceived, lottieDialog);
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
