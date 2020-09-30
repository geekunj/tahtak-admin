package com.sjjs.newsadmin.models;

public class Category {

    private String categoryName;
    private int categoryImgRes;
    private boolean isPublished;

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public Category() {

    }

    public Category(String categoryName, int categoryImgRes) {
        this.categoryName = categoryName;
        this.categoryImgRes = categoryImgRes;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryImgRes() {
        return categoryImgRes;
    }

    public void setCategoryImgRes(int categoryImgRes) {
        this.categoryImgRes = categoryImgRes;
    }

}
