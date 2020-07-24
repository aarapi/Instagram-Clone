package com.instacommerce.annoyingprojects.mobile.ui.afterlogin.userprofile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.instacommerce.annoyingprojects.R;
import com.instacommerce.annoyingprojects.data.UserModel;
import com.instacommerce.annoyingprojects.mobile.basemodels.BaseFragment;
import com.instacommerce.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity;
import com.instacommerce.annoyingprojects.repository.LocalServer;
import com.instacommerce.annoyingprojects.utilities.FragmentUtil;
import com.instacommerce.annoyingprojects.utilities.RequestFunction;
import com.instacommerce.connectionframework.requestframework.constants.MessagingFrameworkConstant;
import com.instacommerce.connectionframework.requestframework.languageData.SavedInformation;
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

import static com.instacommerce.annoyingprojects.mobile.ui.afterlogin.home.LocationBottomSheet.TAG;
import static com.instacommerce.annoyingprojects.utilities.CheckSetup.ServerActions.INSTA_COMMERCE_EDIT_PROFILE;
import static com.instacommerce.annoyingprojects.utilities.Util.setUserImageResPicasso;

public class EditProfileFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private CircleImageView iv_user_profile;

    private TextView tv_cancel;
    private TextView tv_done;
    private TextView tv_change;

    private EditText et_email;
    private EditText et_username;
    private EditText et_phone;

    private RelativeLayout rl_header;
    private ProgressBar progressBar;
    private CheckBox chb_show_contact;

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
        chb_show_contact = containerView.findViewById(R.id.chb_show_contact);

        userModel = LocalServer.getInstance(getContext()).getUser();

    }

    @Override
    public void bindEvents() {
        tv_cancel.setOnClickListener(this::onClick);
        tv_change.setOnClickListener(this::onClick);
        tv_done.setOnClickListener(this::onClick);

        chb_show_contact.setOnCheckedChangeListener(this);
    }

    @Override
    public void setViews() {
        setUserImageResPicasso(userModel.userImage, iv_user_profile);
        et_email.setText(userModel.email);
        et_username.setText(userModel.username);
        et_phone.setText(userModel.phoneNumber);
        chb_show_contact.setChecked(userModel.showContact);
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
                    userModel.showContact = chb_show_contact.isChecked();

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

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName("albilaki")
                            .build();

                    user.updateEmail("test@gmail.com").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(activity, "Updated success", Toast.LENGTH_SHORT).show();
                        }
                    });

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                }
                            });
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
                || isPhotoChanged || chb_show_contact.isChecked() != userModel.showContact) {
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

            setUserImageResPicasso(userModel.userImage,
                    (ImageView) ((HomeActivity) getActivity()).getCv_user_img());
            setUserImageResPicasso(userModel.userImage,
                    (ImageView) ((HomeActivity) getActivity()).getUserProfileFragment().getIv_user_profile());
            ((HomeActivity) getActivity()).getUserProfileFragment().getTv_email().setText(userModel.email);
            ((HomeActivity) getActivity()).getUserProfileFragment().getTv_username().setText(userModel.username);

            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Profile edited", Toast.LENGTH_SHORT).show();
            onBackClicked();
        }
    }


    @Override
    public void onErrorDataReceive(int action, List<Object> data, int status) {
        super.onErrorDataReceive(action, data, status);
        progressBar.setVisibility(View.GONE);
        if (status == MessagingFrameworkConstant.STATUS_CODES.Warning) {
            et_email.setError((String) data.get(0));
        } else {
            et_username.setError((String) data.get(0));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            if (et_phone.getText().toString().equals("")) {
                et_phone.setError("Please add a phone number first.");
                chb_show_contact.setChecked(false);
            }
        }
    }
}