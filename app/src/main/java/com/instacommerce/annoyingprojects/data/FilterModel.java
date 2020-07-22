package com.instacommerce.annoyingprojects.data;

public class FilterModel {

    public String category;
    public String country;
    public String city;
    public boolean allCategories = false;

    private FilterModel() {
    }

    private static FilterModel filterModel;

    public static FilterModel newInstance() {
        if (filterModel == null) {
            filterModel = new FilterModel();
        }
        return filterModel;
    }


}
