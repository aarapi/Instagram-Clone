package com.octopepper.mediapickerinstagram.commons.tasks;

import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;

import com.octopepper.mediapickerinstagram.components.gallery.GalleryPickerFragment;

import java.io.File;
import java.util.ArrayList;

import static com.octopepper.mediapickerinstagram.components.gallery.GalleryPickerFragment.EXTENSION_JPEG;
import static com.octopepper.mediapickerinstagram.components.gallery.GalleryPickerFragment.EXTENSION_JPG;
import static com.octopepper.mediapickerinstagram.components.gallery.GalleryPickerFragment.EXTENSION_PNG;

public class FetchGalleryTask extends AsyncTask<Void, Void, Void> {

    private GalleryPickerFragment galleryPickerFragment;
    private ArrayList<File> mFiles;

    public FetchGalleryTask(GalleryPickerFragment galleryPickerFragment) {
        this.galleryPickerFragment = galleryPickerFragment;
    }

    @Override
    protected void onPreExecute() {
        galleryPickerFragment.getProgress().setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        galleryPickerFragment.setFetchedImages(mFiles);
    }


    @Override
    protected Void doInBackground(Void... voids) {
        fetchMedia();
        return null;
    }

    private void fetchMedia() {
        mFiles = new ArrayList<>();
        File dirDownloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        parseDir(dirDownloads);
        File dirDcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        parseDir(dirDcim);
        File dirPictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        parseDir(dirPictures);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            File dirDocuments = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            parseDir(dirDocuments);
        }
    }

    private void parseDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            parseFileList(files);
        }
    }

    private void parseFileList(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                if (!file.getName().toLowerCase().startsWith(".")) {
                    parseDir(file);
                }
            } else {
                if (file.getName().toLowerCase().endsWith(EXTENSION_JPG)
                        || file.getName().toLowerCase().endsWith(EXTENSION_JPEG)
                        || file.getName().toLowerCase().endsWith(EXTENSION_PNG)) {
                    mFiles.add(file);
                }
            }
        }
    }
}
