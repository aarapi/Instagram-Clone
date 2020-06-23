package com.example.annoyingprojects.mobile.ui.afterlogin.userprofile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.example.connectionframework.requestframework.languageData.SavedInformation;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.annoyingprojects.utilities.CheckSetup.ServerActions.INSTA_COMMERCE_EDIT_PROFILE;
import static com.example.annoyingprojects.utilities.Util.setUserImageResPicasso;

public class EditProfileFragment extends BaseFragment implements View.OnClickListener {

    private CircleImageView iv_user_profile;

    private TextView tv_cancel;
    private TextView tv_done;
    private TextView tv_change;

    private EditText et_email;
    private EditText et_username;
    private EditText et_phone;

    private RelativeLayout rl_header;
    private ProgressBar progressBar;

    private UserModel userModel;

    private boolean isPhotoChanged = false;
    private Bitmap selectedImage = null;

    @Override
    public void initViews() {
        tv_cancel = containerView.findViewById(R.id.tv_cancel);
        tv_done = containerView.findViewById(R.id.tv_done);
        tv_change = containerView.findViewById(R.id.tv_change);

        et_email = containerView.findViewById(R.id.et_email);
        et_username = containerView.findViewById(R.id.et_username);
        et_phone = containerView.findViewById(R.id.et_phone);

        iv_user_profile = containerView.findViewById(R.id.iv_user_profile);
        rl_header = containerView.findViewById(R.id.rl_header);
        progressBar = containerView.findViewById(R.id.progressbar);

        userModel = LocalServer.getInstance(getContext()).getUser();

    }

    @Override
    public void bindEvents() {
        tv_cancel.setOnClickListener(this::onClick);
        tv_change.setOnClickListener(this::onClick);
        tv_done.setOnClickListener(this::onClick);
    }

    @Override
    public void setViews() {
        setUserImageResPicasso(getContext(), userModel.userImage, iv_user_profile);
        et_email.setText(userModel.email);
        et_username.setText(userModel.username);
        et_phone.setText(userModel.phoneNumber);
    }

    @Override
    public int getLayoutId() {
        return R.layout.edit_profile_fragment_layout;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if (view != tv_change){
            if (view == tv_done){
                if (isEdited()){
                    userModel.email = et_email.getText().toString();
                    userModel.username = et_username.getText().toString();
                    userModel.phoneNumber = et_phone.getText().toString();

                    String encodeImage = null;
                    if (isPhotoChanged) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                        byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
                        try {
                            encodeImage = URLEncoder.encode(Base64.getEncoder().encodeToString(byteArrayImage), StandardCharsets.UTF_8.toString());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    sendRequest(RequestFunction.editProfile(0, userModel, encodeImage));
                }
            }
            if (view == tv_cancel) {
                onBackClicked();
            }
        }else {
            ImagePicker.Companion.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        }
    }

    public boolean isEdited(){
        if (!userModel.phoneNumber.equals(et_phone.getText().toString())
                || !userModel.username.equals(et_username.getText().toString())
                || !userModel.email.equals(et_email.getText().toString())
                || isPhotoChanged){
            return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            selectedImage = BitmapFactory.decodeFile(data.getData().getEncodedPath());
            iv_user_profile.setImageBitmap(selectedImage);
            isPhotoChanged = true;
        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), "Error on picker.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackClicked() {
        super.onBackClicked();
        FragmentUtil
                .switchContent(R.id.fl_fragment_container,
                        FragmentUtil.USER_PROFILE_FRAGMENT,
                        (HomeActivity) getContext(),
                        FragmentUtil.AnimationType.SLIDE_DOWN);
    }


    @Override
    public void onDataReceive(int action, List<Object> data) {
        if (action == INSTA_COMMERCE_EDIT_PROFILE){
            Type userSeearchedType = new TypeToken<UserModel>() {
            }.getType();
            Gson gson = new Gson();
            UserModel userModel = gson.fromJson(gson.toJson(data.get(0)), userSeearchedType);
            SavedInformation.getInstance().setPreferenceData(getContext(), userModel, "user");
            RequestFunction.username = LocalServer.getInstance(getContext()).getUser().username;

            setUserImageResPicasso(getContext(), userModel.userImage,
                    (ImageView) ((HomeActivity) getActivity()).getCv_user_img());
            setUserImageResPicasso(getContext(), userModel.userImage,
                    (ImageView) ((HomeActivity) getActivity()).getUserProfileFragment().getIv_user_profile());
            ((HomeActivity) getActivity()).getUserProfileFragment().getTv_email().setText(userModel.email);
            ((HomeActivity) getActivity()).getUserProfileFragment().getTv_username().setText(userModel.username);

            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Profile edited", Toast.LENGTH_SHORT).show();
            onBackClicked();
        }
    }

    @Override
    public void onErrorDataReceive(int action, List<Object> data) {
        super.onErrorDataReceive(action, data);
        progressBar.setVisibility(View.GONE);
    }
}