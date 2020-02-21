package com.example.connectionframework.requestframework.components;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.connectionframework.R;

public class LottieDialog extends Dialog {

    private int animationResId;
    private boolean isCancelable = false;

    public LottieDialog(@NonNull Context context, @NonNull int animationResId){
        super(context);
        this.animationResId = animationResId;
    }

    public LottieDialog(@NonNull Context context, @NonNull int animationResId, boolean isCancelable){
        super(context);
        this.animationResId = animationResId;
        this.isCancelable = isCancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lottie_progress_dialog);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        setCancelable(isCancelable);

        LottieAnimationView lottieAnimationView = findViewById(R.id.lottie);
        lottieAnimationView.setAnimation(animationResId);
        lottieAnimationView.setRepeatMode(LottieDrawable.RESTART);
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        lottieAnimationView.playAnimation();
    }
}