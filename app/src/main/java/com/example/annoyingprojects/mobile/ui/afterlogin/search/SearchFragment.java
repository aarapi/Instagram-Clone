package com.example.annoyingprojects.mobile.ui.afterlogin.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.adapters.RecyclerViewAdapterSearch;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.UserProfileFragment;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseFragment implements TextWatcher, RecyclerViewAdapterSearch.OnItemClickListener {
    private ProgressBar progressBar;

    private EditText serchInput;

    private RecyclerViewAdapterSearch recyclerViewAdapterSearch;
    private RecyclerView rv_user_list;

    private ShimmerFrameLayout shimmer_view_container;

    private ArrayList<UserModel> userModels = new ArrayList<>();

    @Override
    public void initViews() {
        progressBar = containerView.findViewById(R.id.progress);
        serchInput = containerView.findViewById(R.id.edt_search_input);
        rv_user_list = containerView.findViewById(R.id.rv_user_list);
        shimmer_view_container = containerView.findViewById(R.id.shimmer_view_container);

        userModels = LocalServer.newInstance().getUserModels();
    }

    @Override
    public void bindEvents() {
        serchInput.addTextChangedListener(this);
        recyclerViewAdapterSearch.SetOnItemClickListener(this);
    }

    @Override
    public void setViews() {
        serchInput.setSelected(true);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity().getBaseContext());
        rv_user_list.setLayoutManager(linearLayoutManager);
        rv_user_list.setHasFixedSize(true);

        recyclerViewAdapterSearch = new RecyclerViewAdapterSearch(getContext(), userModels);
        rv_user_list.setAdapter(recyclerViewAdapterSearch);
        recyclerViewAdapterSearch.notifyDataSetChanged();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_search_user;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        setUserSearched(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void setUserSearched(String searchString) {
        if (!searchString.equals("")) {
            rv_user_list.setVisibility(View.GONE);
            shimmer_view_container.setVisibility(View.VISIBLE);
            shimmer_view_container.startShimmerAnimation();
            sendRequest(RequestFunction.getSearchedUSer(0, searchString));
        }
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        Type userSeearchedType = new TypeToken<ArrayList<UserModel>>() {
        }.getType();
        Gson gson = new Gson();
        ArrayList<UserModel> userModels = gson.fromJson(gson.toJson(data.get(0)), userSeearchedType);
        recyclerViewAdapterSearch.setUserModels(userModels);
        shimmer_view_container.setVisibility(View.GONE);
        rv_user_list.setVisibility(View.VISIBLE);
    }
    @Override
    public void onBackClicked() {
        FragmentUtil
                .switchContent(R.id.fl_fragment_container,
                        FragmentUtil.HOME_FRAGMENT,
                        (HomeActivity) getContext(),
                        null);
        ((HomeActivity) getContext()).setHomeIcon();
    }

    @Override
    public void onItemClick(View view, int position) {
        UserModel userModel;
        userModel = userModels.get(position);
        LocalServer.newInstance().setUserModels(userModel);

        Bundle args = new Bundle();
        args.putSerializable(UserProfileFragment.USER_PROFILE_DATA, (Serializable) userModel);
        UserProfileFragment userProfileFragment = UserProfileFragment.newInstance(args);

        FragmentUtil.switchFragmentWithAnimation(R.id.fl_fragment_container,
                userProfileFragment,
                (HomeActivity) getContext(),
                FragmentUtil.USER_PROFILE_FRAGMENT,
                null);
    }
}
