package com.instacommerce.annoyingprojects.mobile.ui.afterlogin.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.instacommerce.annoyingprojects.R;
import com.instacommerce.annoyingprojects.data.PostModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class LocationBottomSheet extends BottomSheetDialogFragment
        implements View.OnClickListener {
    public static final String TAG = "LOCATION_BOTTOM_SHEET";
    private ItemClickListener mListener;

    private TextView tv_product_name;
    private TextView tv_username;
    private TextView tv_product_price;
    private TextView tv_location;

    public static LocationBottomSheet newInstance(Bundle args) {
        LocationBottomSheet locationBottomSheet = new LocationBottomSheet();
        locationBottomSheet.setArguments(args);
        return locationBottomSheet;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.location_bottom_sheet_layout, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_product_name = view.findViewById(R.id.tv_product_name);
        tv_username = view.findViewById(R.id.tv_username);
        tv_product_price = view.findViewById(R.id.tv_product_price);
        tv_location = view.findViewById(R.id.tv_location);

        PostModel postModel = (PostModel) getArguments().getSerializable("PRODUCT_DATA");

        tv_product_name.setText(postModel.getProductName());
        tv_username.setText(postModel.getUserName());
        tv_location.setText(postModel.getCity()+" "+postModel.getCountry());
        tv_product_price.setText(postModel.getProductPrice());
    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof ItemClickListener) {
//            mListener = (ItemClickListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement ItemClickListener");
//        }
//    }
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
    @Override public void onClick(View view) {

    }
    public interface ItemClickListener {
        void onItemClick(String item);
    }
}