package com.example.annoyingprojects.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeFragment;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.utilities.RequestFunction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class BitmapTask extends AsyncTask<String, String, String> {

    private HomeFragment homeFragment;
    private List<Object> newPost;

    public BitmapTask(HomeFragment homeFragment, List<Object> newPost) {
        this.homeFragment = homeFragment;
        this.newPost = newPost;
    }

    @Override
    protected void onPostExecute(String string) {

    }

    @Override
    public void onProgressUpdate(String... values) {
        homeFragment.getProgress().setProgress(Integer.parseInt(values[0]));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(String... args) {
        List<File> imageFiles = (ArrayList) newPost.get(3);
        int size = imageFiles.size();

        List<String> postData = new ArrayList<>();
        postData.add((String) newPost.get(0));
        postData.add((String) newPost.get(1));
        postData.add((String) newPost.get(2));
        postData.add((String) newPost.get(4));

        for (int i = 0; i < size; i++) {


            Bitmap selectedImage = BitmapFactory.decodeFile(imageFiles.get(i).getAbsolutePath());
            publishProgress("0");
            ((HomeActivity) homeFragment.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    homeFragment.getIv_upload().setImageBitmap(selectedImage);
                }
            });

            publishProgress("2");
            publishProgress("10");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            publishProgress("15");
            selectedImage.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
            publishProgress("30");
            byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
            publishProgress("60");
            String encodeImage = null;
            try {
                encodeImage = URLEncoder.encode(Base64.getEncoder().encodeToString(byteArrayImage), StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            publishProgress("70");

            postData.add(encodeImage);


            publishProgress("80");
        }
        UserModel userModel = LocalServer.getInstance(homeFragment.getContext()).getUser();

        homeFragment.sendRequest(RequestFunction
                .createNewPost(0, postData, userModel.country, userModel.city));
        ;
        publishProgress("90");

        return null;
    }
}
