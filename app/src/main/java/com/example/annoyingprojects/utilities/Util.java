package com.example.annoyingprojects.utilities;

import android.content.Context;
import android.widget.ImageView;

import com.example.annoyingprojects.R;
import com.squareup.picasso.Picasso;

public class Util {
    public static void setUserImageRes(Context context, String url, ImageView view){
        Picasso.with(context).load(url)
                .placeholder(R.drawable.placeholder_error_media).error(R.drawable.placeholder_error_media).fit()
                .centerCrop().into(view);
    }
}
