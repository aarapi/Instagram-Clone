package com.instacommerce.connectionframework.requestframework.httpconnection;

import com.instacommerce.connectionframework.requestframework.constants.Constants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    public static String getResponse(String messageServiceUrl, String jsonFormatedRequest) throws HttpConnectionException{
        try {
        DataOutputStream wr = null;
        BufferedReader in = null;

        String urlParameters  = "request="+ jsonFormatedRequest;
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("request", jsonFormatedRequest);
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;



            URL url = new URL(messageServiceUrl);
            HttpURLConnection connection  = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            connection.setUseCaches( false );

            connection.setConnectTimeout(Constants.Application.CONNECTION_TIMEOUT);
            connection.setReadTimeout(Constants.Application.CONNECTION_TIMEOUT);


            connection.setDoOutput(true);

            wr = new DataOutputStream(connection.getOutputStream());
            wr.write(postData);
            wr.flush();
            wr.close();
            in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            connection.disconnect();

            return response.toString();

        } catch (SocketTimeoutException sc){
            throw ( new HttpConnectionException(Constants.Application.CONNECTION_TIMED_OUT_ERROR_MESSAGE));
        }
        catch (IOException e) {
            throw (new HttpConnectionException(Constants.Application.CONNECTION_OTHER_EXCEPTION));
        }

    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
