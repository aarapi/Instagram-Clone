package com.octopepper.mediapickerinstagram.commons.modules;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;

public class LoadMoreModule {

    private RecyclerView recyclerView;
    private LoadMoreModuleDelegate delegate;

    private static final Object TAG = new Object();
    private static final int SETTLING_DELAY = 300;

    private Picasso sPicasso = null;
    private Runnable mSettlingResumeRunnable = null;

    private Context context;

    public void LoadMoreUtils(RecyclerView r, LoadMoreModuleDelegate d, Context context) {
        this.recyclerView = r;
        this.delegate = d;
        this.context = context;

        if (sPicasso == null) {
            sPicasso = Picasso.with(context);
        }

        addListener();
    }

    private void addListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (llm.findFirstVisibleItemPosition() >= (llm.getItemCount() / 5)) {
                    delegate.shouldLoadMore();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    sPicasso.resumeTag(context);
                } else {
                    sPicasso.pauseTag(context);
                }
            }
        });
    }

}
