package com.example.fragments.fragments;

import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragments.basemodels.BaseActivity;
import com.example.fragments.basemodels.BaseFragment;
import com.example.fragments.R;
import com.example.fragments.data.CurrencyModel;

import java.util.List;

public class FragmentCurrencyList extends BaseFragment {
    public static final int EXTRA_REVEAL_CENTER_PADDING = 40;
    private BaseActivity activity;

    private RecyclerView rv_currency_list;
    private List<CurrencyModel> currencyModels;

    private RecyclerView.LayoutManager layoutManager;

    public FragmentCurrencyList(BaseActivity activity) {
        super(R.layout.fragment_list_currency);
        this.activity = activity;
    }

    @Override
    public void initViews() {
        rv_currency_list = containerView.findViewById(R.id.rv_currency_list);
    }

    @Override
    public void setViews() {

    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.test_menu, menu);
        setupSearchView(menu);
    }

    private void setupSearchView(Menu menu) {
    }

    public void onDataRecive(int action, Object data) {

    }

    @Override
    public void sendRequestMessage() {
    }

    public List<CurrencyModel> getCurrencyModels() {
        return currencyModels;
    }
}


