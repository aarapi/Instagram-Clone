package com.octopepper.mediapickerinstagram;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.octopepper.mediapickerinstagram.commons.adapters.ViewPagerAdapter;
import com.octopepper.mediapickerinstagram.commons.models.Session;
import com.octopepper.mediapickerinstagram.commons.models.enums.SourceType;
import com.octopepper.mediapickerinstagram.commons.modules.PermissionModule;
import com.octopepper.mediapickerinstagram.commons.tasks.FetchGalleryTask;
import com.octopepper.mediapickerinstagram.commons.ui.ToolbarView;
import com.octopepper.mediapickerinstagram.components.NewPostFragment;
import com.octopepper.mediapickerinstagram.components.gallery.GalleryPickerFragment;
import com.octopepper.mediapickerinstagram.components.photo.CapturePhotoFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ToolbarView.OnClickTitleListener,
        ToolbarView.OnClickNextListener, ToolbarView.OnClickBackListener {

    @BindView(R2.id.mMainTabLayout)
    TabLayout mMainTabLayout;
    @BindView(R2.id.mMainViewPager)
    ViewPager mMainViewPager;
    @BindView(R2.id.mToolbar)
    ToolbarView mToolbar;

    @BindString(R2.string.tab_gallery)
    String _tabGallery;
    @BindString(R2.string.tab_photo)
    String _tabPhoto;



    private FrameLayout fr_fragment_container;
    private boolean isNewPost = false;
    private Session mSession = Session.getInstance();
    private HashSet<SourceType> mSourceTypeSet = new HashSet<>();

    private NewPostFragment newPostFragment;
    private GalleryPickerFragment galleryPickerFragment;

    private ViewPagerAdapter pagerAdapter;

    private void initViews() {
        PermissionModule permissionModule = new PermissionModule(this);
        permissionModule.checkPermissions();

        mToolbar.setOnClickBackMenuListener(this)
                .setOnClickTitleListener(this)
                .setOnClickNextListener(this);


        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getListFragment());
        mMainViewPager.setAdapter(pagerAdapter);

        mMainTabLayout.addOnTabSelectedListener(getViewPagerOnTabSelectedListener());
        mMainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMainTabLayout));

        mMainViewPager.setCurrentItem(0);
    }

    private TabLayout.ViewPagerOnTabSelectedListener getViewPagerOnTabSelectedListener() {
        return new TabLayout.ViewPagerOnTabSelectedListener(mMainViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                displayTitleByTab(tab);
                initNextButtonByTab(tab.getPosition());
            }
        };
    }

    private void displayTitleByTab(TabLayout.Tab tab) {
        if (tab.getText() != null) {
            String title = tab.getText().toString();
            mToolbar.setTitle(title);
        }
    }

    private void initNextButtonByTab(int position) {
        switch (position) {
            case 0:
                mToolbar.showNext();
                break;
            case 1:
                mToolbar.hideNext();
                break;
            case 2:
                mToolbar.hideNext();
                break;
            default:
                mToolbar.hideNext();
                break;
        }
    }

    private ArrayList<Fragment> getListFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        if (mSourceTypeSet.contains(SourceType.Gallery)) {
            galleryPickerFragment = GalleryPickerFragment.newInstance();
            fragments.add(galleryPickerFragment);

            mMainTabLayout.addTab(mMainTabLayout.newTab().setText(_tabGallery));
        }

        return fragments;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        ButterKnife.bind(this);

        // If you want to start activity with custom Tab
        mSourceTypeSet.add(SourceType.Gallery);
        initViews();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (arePermissionsGranted(grantResults)) {
//            pagerAdapter.setFragments(getListFragment());
//            mMainViewPager.setAdapter(pagerAdapter);
//
//            mMainTabLayout.addOnTabSelectedListener(getViewPagerOnTabSelectedListener());
//            mMainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMainTabLayout));
//            mMainViewPager.setCurrentItem(0);
//            pagerAdapter.notifyDataSetChanged();
        } else {
            finish();
        }
    }

    private boolean arePermissionsGranted(int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == -1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClickBack() {
        if (!isNewPost) {
            finish();
        }else {
            isNewPost = false;
            fr_fragment_container.setVisibility(View.GONE);
            mMainTabLayout.setVisibility(View.VISIBLE);
            mMainViewPager.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickNext() {


        if (!isNewPost) {
            if (mSession.getFilesToUpload().size() == 0) {
                Toast.makeText(this, "Zgjidhni nje foto", Toast.LENGTH_SHORT).show();
            } else {
                isNewPost = true;
                Bundle args = new Bundle();
                args.putSerializable("filePath", (Serializable) mSession.getFilesToUpload());
                newPostFragment = NewPostFragment.newInstance(args);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fr_fragment_container, newPostFragment)
                        .commit();
                fr_fragment_container = findViewById(R.id.fr_fragment_container);
                fr_fragment_container.setVisibility(View.VISIBLE);
                mMainTabLayout.setVisibility(View.GONE);
                mMainViewPager.setVisibility(View.GONE);
            }
        }
        else {
            // Fetch file to upload
            if (newPostFragment.validateInputs()) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", (Serializable) newPostFragment.getPostData());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }


    }

    @Override
    public void onClickTitle() {

    }



}
