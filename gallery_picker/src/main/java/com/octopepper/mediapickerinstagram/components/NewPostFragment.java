package com.octopepper.mediapickerinstagram.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.octopepper.mediapickerinstagram.R;
import com.octopepper.mediapickerinstagram.components.video.CaptureVideoFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class NewPostFragment extends Fragment {
    private ImageView iv_post_img;
    private EditText et_write_caption;

    public static NewPostFragment newInstance(Bundle bundleArgs) {
        NewPostFragment newPostFragment = new NewPostFragment();
        newPostFragment.setArguments(bundleArgs);
        return newPostFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sharepost_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       String filePath = (String) getArguments().get("filePath");
        initViews(view);
        setImageRes(filePath);

    }

    public void initViews(View view){
        iv_post_img = view.findViewById(R.id.iv_post_img);
        et_write_caption  = view.findViewById(R.id.et_write_caption);
    }
    public void setImageRes(String filePath){
        Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
        iv_post_img.setImageBitmap(selectedImage);

    }

    public List<String> getPostData(){
        List<String> postData = new ArrayList<>();

        Bitmap selectedImage = BitmapFactory.decodeFile((String) getArguments().get("filePath"));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        postData.add(encodeImage);
        postData.add(et_write_caption.getText().toString());
        return postData;
    }



}