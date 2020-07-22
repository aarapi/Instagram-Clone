package com.instacommerce.annoyingprojects.mobile.ui.afterlogin.userprofile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.palette.graphics.Palette;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.instacommerce.annoyingprojects.R;
import com.instacommerce.annoyingprojects.data.StoryModel;
import com.instacommerce.annoyingprojects.mobile.basemodels.BaseFragment;
import com.instacommerce.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity;
import com.instacommerce.annoyingprojects.tasks.StoryTask;
import com.instacommerce.annoyingprojects.utilities.CheckSetup;
import com.instacommerce.annoyingprojects.utilities.FragmentUtil;
import com.octopepper.mediapickerinstagram.commons.models.CurrencyList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddUserStoryFragment extends BaseFragment implements View.OnClickListener {


    private RelativeLayout root;
    private Spinner sp_currency;
    private ProgressBar progressbar;

    private TextView tv_done;
    private TextView tv_cancel;

    private EditText et_product_name;
    private EditText et_product_price;
    private ImageView iv_story_img;

    private Bitmap selectedBitmap;


    private AddUserStoryFragment addUserStoryFragment;

    public static AddUserStoryFragment newInstance(Bundle args) {
        AddUserStoryFragment fragment = new AddUserStoryFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void initViews() {
        root = containerView.findViewById(R.id.root);
        sp_currency = containerView.findViewById(R.id.sp_currency);
        progressbar = containerView.findViewById(R.id.progressbar);

        tv_done = containerView.findViewById(R.id.tv_done);
        tv_cancel = containerView.findViewById(R.id.tv_cancel);

        et_product_name = containerView.findViewById(R.id.et_product_name);
        et_product_price = containerView.findViewById(R.id.et_product_price);
        iv_story_img = containerView.findViewById(R.id.iv_story_img);

        ((HomeActivity) getContext()).getLl_bottom_menu().setVisibility(View.GONE);
        addUserStoryFragment = this;
    }

    @Override
    public void bindEvents() {
        tv_done.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void setViews() {
        String imagePath = (String) getArguments().getSerializable("SELECTED_IMAGE");
        selectedBitmap = BitmapFactory.decodeFile(imagePath);

        MyAdapterSpinner adapter = new MyAdapterSpinner(getContext(),
                com.octopepper.mediapickerinstagram.R.layout.spinner_item, CurrencyList.currencyList, CurrencyList.countriesFlag);
        sp_currency.setAdapter(adapter);

        setBitmapImage(selectedBitmap);
    }

    @Override
    public int getLayoutId() {
        return R.layout.add_user_story_fragment_layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if (view == tv_done) {
            if (isInputValid()) {
                progressbar.setVisibility(View.VISIBLE);
                tv_done.setVisibility(View.INVISIBLE);

                Object[] data = new Object[]{selectedBitmap, et_product_name.getText().toString(),
                        et_product_price.getText().toString().concat(sp_currency.getSelectedItem().toString())};

                StoryTask storyTask = new StoryTask(addUserStoryFragment);
                storyTask.execute(data);

            }
        } else if (view == tv_cancel) {
            onBackClicked();
        }
    }

    private boolean isInputValid() {

        if ("".equals(et_product_price.getText().toString())) {
            et_product_price.setError("Please add product price");
            return false;
        }

        if ("".equals(et_product_name.getText().toString())) {
            et_product_name.setError("Please add product name");
            return false;
        }

        return true;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        iv_story_img.setImageBitmap(bitmapImage);
        Palette.from(bitmapImage)
                .generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch textSwatch = palette.getVibrantSwatch();
                        if (textSwatch == null) {
                            root.setBackgroundColor(getContext()
                                    .getResources().getColor(R.color.black));
                            return;
                        }
                        root.setBackgroundColor(textSwatch.getRgb());
                    }
                });

    }

    @Override
    public void onBackClicked() {
        super.onBackClicked();

        FragmentUtil
                .switchContent(R.id.fl_fragment_container,
                        FragmentUtil.USER_PROFILE_FRAGMENT,
                        (HomeActivity) getContext(),
                        FragmentUtil.AnimationType.SLIDE_DOWN);

        ((HomeActivity) getContext()).getLl_bottom_menu().setVisibility(View.VISIBLE);
    }


    @Override
    public void onDataReceive(int action, List<Object> data) {
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_CREATE_NEW_STORY) {
            progressbar.setVisibility(View.INVISIBLE);

            Gson gson = new Gson();
            Type storiesType = new TypeToken<StoryModel>() {
            }.getType();
            StoryModel storyModel = gson.fromJson(gson.toJson(data.get(0)), storiesType);
            ((HomeActivity) getActivity()).getHomeFragment().addNewStory(storyModel);
            onBackClicked();
            showMessage("Story upload sucessfully");
        }
    }

    @Override
    public void onErrorDataReceive(int action, List<Object> data) {
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_CREATE_NEW_STORY) {
            progressbar.setVisibility(View.INVISIBLE);
            tv_done.setVisibility(View.VISIBLE);
            showMessage("Story couldn't uploaded");
        }
    }


    private void showMessage(String message) {
        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    protected class MyAdapterSpinner extends ArrayAdapter {

        int[] Image;
        String[] Text;

        public MyAdapterSpinner(Context context, int resource, String[] text, int[] image) {
            super(context, resource, text);
            Image = image;
            Text = text;
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(com.octopepper.mediapickerinstagram.R.layout.spinner_item, parent, false);

            //Set Custom View
            TextView tv = (TextView) view.findViewById(com.octopepper.mediapickerinstagram.R.id.textView);
            ImageView img = (ImageView) view.findViewById(com.octopepper.mediapickerinstagram.R.id.imageView);

            tv.setText(Text[position]);
            img.setImageResource(Image[position]);

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }
}