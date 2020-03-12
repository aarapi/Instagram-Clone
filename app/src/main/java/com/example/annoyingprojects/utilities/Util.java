package com.example.annoyingprojects.utilities;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class Util {

    public static void setUserImageRes(Context context, String url, ImageView view){
        Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(400))
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                .into(view);
    }
}
