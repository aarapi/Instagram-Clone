package com.example.annoyingprojects.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.AddUserStoryFragment;
import com.example.annoyingprojects.utilities.RequestFunction;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class StoryTask extends AsyncTask {

    private AddUserStoryFragment addUserStoryFragment;

    public StoryTask(AddUserStoryFragment addUserStoryFragment) {
        this.addUserStoryFragment = addUserStoryFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Object doInBackground(Object[] objects) {
        String encodeImage = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ((Bitmap) objects[0]).compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
        try {
            encodeImage = URLEncoder.encode(Base64.getEncoder().encodeToString(byteArrayImage), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        List<String> storyData = new ArrayList<>();

        storyData.add(encodeImage);
        storyData.add((String) objects[1]);
        storyData.add((String) objects[2]);

        addUserStoryFragment.sendRequest(RequestFunction.createStory(0, storyData));

        return null;
    }

}
