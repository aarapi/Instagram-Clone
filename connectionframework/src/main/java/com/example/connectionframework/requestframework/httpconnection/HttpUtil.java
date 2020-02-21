package com.example.connectionframework.requestframework.httpconnection;

import com.example.connectionframework.requestframework.constants.Constants;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class HttpUtil {
    public static String getResponse(String messageServiceUrl, String jsonFormatedRequest) throws HttpConnectionException{
        try {
        DataOutputStream wr = null;
        BufferedReader in = null;

        String urlParameters  = "request="+ jsonFormatedRequest;
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
}
