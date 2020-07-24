package com.octopepper.mediapickerinstagram.components.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.octopepper.mediapickerinstagram.R;
import com.octopepper.mediapickerinstagram.R2;
import com.octopepper.mediapickerinstagram.commons.modules.ReboundModule;
import com.octopepper.mediapickerinstagram.commons.modules.ReboundModuleDelegate;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MediaItemView extends RelativeLayout implements ReboundModuleDelegate {

    @BindView(R2.id.mMediaThumb)
    ImageView mMediaThumb;
    private CheckBox ch_image;

    private boolean isImageChecked;

    private Context context;

    private File mCurrentFile;
    private ReboundModule mReboundModule = ReboundModule.getInstance(this);
    private WeakReference<MediaItemViewListener> mWrListener;

    void setListener(MediaItemViewListener listener) {
        this.mWrListener = new WeakReference<>(listener);
    }

    public MediaItemView(Context context) {
        super(context);
        this.context = context;
        isImageChecked = false;
        View v = View.inflate(context, R.layout.media_item_view, this);
        ch_image = v.findViewById(R.id.ch_image);
        ch_image.setClickable(false);
        ButterKnife.bind(this, v);
    }

    public void bind(File file) {
        mCurrentFile = file;
        mReboundModule.init(mMediaThumb);
        Bitmap currentImageBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        mMediaThumb.setImageBitmap(currentImageBitmap);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public void onTouchActionUp() {
        if (!isImageChecked) {
            if (GridAdapter.imagesCounter < 4) {
                ch_image.setChecked(true);
                isImageChecked = true;
                GridAdapter.imagesCounter++;
                ch_image.setVisibility(VISIBLE);
                mWrListener.get().onClickItem(mCurrentFile, !isImageChecked);
            } else
                Toast.makeText(context, "Keni arritur limitin", Toast.LENGTH_SHORT).show();

        } else {
            ch_image.setChecked(false);
            isImageChecked = false;
            GridAdapter.imagesCounter--;
            ch_image.setVisibility(GONE);
            mWrListener.get().onClickItem(mCurrentFile, !isImageChecked);
        }

    }
}
