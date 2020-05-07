package com.octopepper.mediapickerinstagram.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.octopepper.mediapickerinstagram.R;
import com.octopepper.mediapickerinstagram.commons.models.CurrencyList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewPostFragment extends Fragment {
    private List<File> imageFiles;

    private ImageView iv_post_img;

    private EditText et_write_caption;
    private EditText et_product_name;
    private EditText et_product_price;

    private Spinner sp_currency;

    public static NewPostFragment newInstance(Bundle bundleArgs) {
        NewPostFragment newPostFragment = new NewPostFragment();
        newPostFragment.setArguments(bundleArgs);
        return newPostFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sharepost_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageFiles = (ArrayList<File>) getArguments().get("filePath");
        initViews(view);
        setViews();
        setImageRes(imageFiles);

    }

    public void initViews(View view){
        iv_post_img = view.findViewById(R.id.iv_post_img);

        et_write_caption  = view.findViewById(R.id.et_write_caption);
        et_product_name = view.findViewById(R.id.et_product_name);
        et_product_price = view.findViewById(R.id.et_product_price);

        sp_currency = view.findViewById(R.id.sp_currency);
    }

    public void setViews() {
        MyAdapterSpinner adapter = new MyAdapterSpinner(getContext(),
                R.layout.spinner_item, CurrencyList.currencyList, CurrencyList.countriesFlag);
        sp_currency.setAdapter(adapter);
    }

    public void setImageRes(List<File> imageFiles) {
        Bitmap selectedImage = BitmapFactory.decodeFile(imageFiles.get(0).getAbsolutePath());
        iv_post_img.setImageBitmap(selectedImage);

    }

    public List<Object> getPostData() {

        List<Object> postData = new ArrayList<>();
        postData.add(et_write_caption.getText().toString());
        postData.add(et_product_name.getText().toString());
        postData.add(et_product_price.getText()
                .toString() + " " + CurrencyList.currencyList[sp_currency.getSelectedItemPosition()]);
        postData.add(imageFiles);

        return postData;
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
            View view = inflater.inflate(R.layout.spinner_item, parent, false);

            //Set Custom View
            TextView tv = (TextView) view.findViewById(R.id.textView);
            ImageView img = (ImageView) view.findViewById(R.id.imageView);

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
