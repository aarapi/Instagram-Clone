package com.octopepper.mediapickerinstagram.commons.models;

/*
 * Created by Guillaume on 21/11/2016.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Session {

    private static Session sInstance = null;
    private List<File> mFileToUpload = new ArrayList<>();

    private Session() {
    }

    public static Session getInstance() {
        if (sInstance == null) {
            sInstance = new Session();
        }
        return sInstance;
    }

    public List<File> getFilesToUpload() {
        return mFileToUpload;
    }

    public void setFileToUpload(List<File> fileToUpload) {
        mFileToUpload = fileToUpload;
    }

}