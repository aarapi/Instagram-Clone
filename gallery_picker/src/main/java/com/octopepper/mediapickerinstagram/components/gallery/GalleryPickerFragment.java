package com.octopepper.mediapickerinstagram.components.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.octopepper.mediapickerinstagram.R;
import com.octopepper.mediapickerinstagram.R2;
import com.octopepper.mediapickerinstagram.commons.models.Session;
import com.octopepper.mediapickerinstagram.commons.modules.LoadMoreModule;
import com.octopepper.mediapickerinstagram.commons.modules.LoadMoreModuleDelegate;
import com.octopepper.mediapickerinstagram.commons.tasks.FetchGalleryTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;


public class GalleryPickerFragment extends Fragment implements GridAdapterListener, LoadMoreModuleDelegate {

    @BindView(R2.id.mGalleryRecyclerView)
    RecyclerView mGalleryRecyclerView;
    @BindView(R2.id.mPreview)
    ImageView mPreview;
    @BindView(R2.id.mAppBarContainer)
    LinearLayout mAppBarContainer;

    public static final String EXTENSION_JPG = ".jpg";
    public static final String EXTENSION_JPEG = ".jpeg";
    public static final String EXTENSION_PNG = ".png";
    private static final int PREVIEW_SIZE = 800;
    private static final int MARGING_GRID = 2;
    private static final int RANGE = 20;

    List<File> files = new ArrayList<>();

    private Session mSession = Session.getInstance();
    private LoadMoreModule mLoadMoreModule = new LoadMoreModule();
    private GridAdapter mGridAdapter;
    private ArrayList<File> mFiles;
    private boolean isLoading = false;
    private int mOffset;
    private boolean isFirstLoad = true;

    private ProgressBar progress;

    public static GalleryPickerFragment newInstance() {
        return new GalleryPickerFragment();
    }

    private void initViews() {
        if (isFirstLoad) {
            mGridAdapter = new GridAdapter(getContext());
        }
        mGridAdapter.setListener(this);
        mGalleryRecyclerView.setAdapter(mGridAdapter);
        mGalleryRecyclerView.setHasFixedSize(true);
        mGalleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mGalleryRecyclerView.addItemDecoration(addItemDecoration());
        mLoadMoreModule.LoadMoreUtils(mGalleryRecyclerView, this, getContext());
        mOffset = 0;

        FetchGalleryTask fetchGalleryTask = new FetchGalleryTask(this);
        fetchGalleryTask.executeOnExecutor(THREAD_POOL_EXECUTOR);
    }

    private RecyclerView.ItemDecoration addItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent, RecyclerView.State state) {
                outRect.left = MARGING_GRID;
                outRect.right = MARGING_GRID;
                outRect.bottom = MARGING_GRID;
                if (parent.getChildLayoutPosition(view) >= 0 && parent.getChildLayoutPosition(view) <= 3) {
                    outRect.top = MARGING_GRID;
                }
            }
        };
    }

    private void fetchMedia() {
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

    public void setFetchedImages(ArrayList<File> files) {
        this.mFiles = files;
        if (mFiles.size() > 0) {
            displayPreview(mFiles.get(0));
            mGridAdapter.setItems(getRangePets());
        }
        isFirstLoad = false;
        progress.setVisibility(View.GONE);
    }

    private List<File> getRangePets() {
        if (mOffset < mFiles.size()) {
            if ((mOffset + RANGE) < mFiles.size()) {
                return mFiles.subList(mOffset, mOffset + RANGE);
            } else if ((mOffset + RANGE) >= mFiles.size()) {
                return mFiles.subList(mOffset, mFiles.size());
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
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

    private void loadNext() {
        if (!isLoading) {
            isLoading = true;
            mOffset += RANGE;
            List<File> files = new ArrayList<>();
            files.addAll(getRangePets());
            if (files.size() > 0) {
                mGridAdapter.addItems(files, mGridAdapter.getItemCount());
            }
            isLoading = false;
        }
    }

    private void displayPreview(File file) {
        Bitmap currentImageBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        mPreview.setImageBitmap(currentImageBitmap);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gallery_picker_view, container, false);
        progress = v.findViewById(R.id.progress);
        ButterKnife.bind(this, v);
        initViews();
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        Picasso.get().cancelRequest(mPreview);
    }

    @Override
    public void onClickMediaItem(File file, boolean isImageClicked) {
        displayPreview(file);
        if (isImageClicked) {
            files.remove(file);
        } else {
            files.add(file);
        }
        mSession.setFileToUpload(files);
    }

    @Override
    public void shouldLoadMore() {
        loadNext();
    }

    public ProgressBar getProgress() {
        return progress;
    }
}
