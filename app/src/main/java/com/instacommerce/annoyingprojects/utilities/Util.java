package com.instacommerce.annoyingprojects.utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.instacommerce.annoyingprojects.R;
import com.squareup.picasso.Picasso;


public class Util {
    public static void setUserImageResPicasso(String url, ImageView view) {
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

    public static   boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;  //<--  --  -- Connected
                    }
                }
            }
        }
        return false;  //<--  --  -- NOT Connected
    }


}
