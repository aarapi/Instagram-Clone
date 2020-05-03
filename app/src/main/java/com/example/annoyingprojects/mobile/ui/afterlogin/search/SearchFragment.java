package com.example.annoyingprojects.mobile.ui.afterlogin.search;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.adapters.RecyclerViewAdapterSearch;
import com.example.annoyingprojects.data.User;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseFragment implements TextWatcher {
    private ProgressBar progressBar;

    private EditText serchInput;

    private RecyclerViewAdapterSearch recyclerViewAdapterSearch;
    private RecyclerView rv_user_list;

    private ArrayList<User> users = new ArrayList<>();

    @Override
    public void initViews() {
        progressBar = containerView.findViewById(R.id.progress);
        serchInput = containerView.findViewById(R.id.edt_search_input);
        rv_user_list = containerView.findViewById(R.id.rv_user_list);
    }

    @Override
    public void bindEvents() {
        serchInput.addTextChangedListener(this);
    }

    @Override
    public void setViews() {
        serchInput.setSelected(true);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity().getBaseContext());
        rv_user_list.setLayoutManager(linearLayoutManager);
        rv_user_list.setHasFixedSize(true);

        recyclerViewAdapterSearch = new RecyclerViewAdapterSearch(getContext(), users);
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
        if (!searchString.equals(""))
            sendRequest(RequestFunction.getSearchedUSer(0, searchString));
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        Type userSeearchedType = new TypeToken<ArrayList<User>>() {
        }.getType();
        Gson gson = new Gson();

        ArrayList<User> users = gson.fromJson(gson.toJson(data.get(0)), userSeearchedType);

        recyclerViewAdapterSearch.setUsers(users);
        recyclerViewAdapterSearch.SetOnItemClickListener(new RecyclerViewAdapterSearch.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

        recyclerViewAdapterSearch.notifyDataSetChanged();
    }
}
