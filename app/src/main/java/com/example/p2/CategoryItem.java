package com.example.p2;

import java.util.List;

public class CategoryItem {
    private String categoryName;
    private List<karbar> users;

    public CategoryItem(String categoryName, List<karbar> users) {
        this.categoryName = categoryName;
        this.users = users;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<karbar> getUsers() {
        return users;
    }
}