package com.example.annoyingprojects.mobile.ui.afterlogin.home;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.CategoryModel;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.utilities.LocationUtil;

import java.util.ArrayList;
import java.util.Collections;

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static String FILTER_DIALOG_FRAGMENT = "FILTER_DIALOG_FRAGMENT";

    private Spinner sp_categories;
    private Spinner sp_countries;
    private Spinner sp_cities;

    private Button bt_cancel;
    private Button btn_apply;

    public FilterDialogFragment() {
    }



    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_dialog_fragment_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sp_categories = view.findViewById(R.id.sp_categories);
        sp_countries = view.findViewById(R.id.sp_countries);
        sp_cities = view.findViewById(R.id.sp_cities);

        btn_apply = view.findViewById(R.id.btn_apply);
        bt_cancel = view.findViewById(R.id.bt_cancel);

        btn_apply.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);

        setCategoriesList();
        setCountriesList();
    }

    public void setCategoriesList(){
        ArrayList<CategoryModel> categoryModels = LocalServer.newInstance().getCategoryModels();
        int size = categoryModels.size();
        ArrayList<String> categories = new ArrayList<String>();

        for (int i =0; i<size; i++){
            categories.add(categoryModels.get(i).categoryName);
        }

        Collections.sort(categories);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, categories);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the your spinner
        sp_categories.setAdapter(categoryAdapter);

    }
    public void setCountriesList(){
        String[] countries = LocationUtil.newInstance(getContext()).getCountries();
               ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the your spinner
        sp_countries.setAdapter(countryAdapter);
        sp_countries.setOnItemSelectedListener(this);
    }
    public void setCityList(String countryName){
        ArrayList<String> cites = LocationUtil.newInstance(getContext())
                .getListOfCities(countryName);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, cites);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the your spinner
        sp_cities.setAdapter(cityAdapter);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view == btn_apply){

        }
        dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        setCityList(LocationUtil.newInstance(getContext()).getCountries()[i]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}