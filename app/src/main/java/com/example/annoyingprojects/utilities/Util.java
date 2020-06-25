package com.example.annoyingprojects.utilities;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.annoyingprojects.R;
import com.squareup.picasso.Picasso;

public class Util {
    public static void setUserImageResPicasso(Context context, String url, ImageView view) {
        Picasso.get().load(url)
                .placeholder(R.drawable.placeholder_error_media)
                .error(R.drawable.placeholder_error_media)
                .noFade()
                .fit()
                .centerCrop().into(view);
    }

    public static void setUserImageResGlide(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .into(view);
    }

}
