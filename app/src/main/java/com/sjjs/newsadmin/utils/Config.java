package com.sjjs.newsadmin.utils;

import android.text.TextUtils;

import com.sjjs.newsadmin.models.Banner;
import com.sjjs.newsadmin.models.Category;
import com.sjjs.newsadmin.models.News;

import java.util.ArrayList;

public class Config {

    public static News newsState;
    public static ArrayList<String> globalCategoryList;
    public static Banner bannerState;

    public static Boolean isNewsItemContainsEmpty() {

        if (TextUtils.isEmpty(newsState.getNewsTitle())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getShortDescription())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getNewsCategory())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getNewsImageUrl())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getAuthor())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getDatePublished())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getNewsContent())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getSearchKeywords())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getVideoUrl())) {
            return false;
        } else {
            return true;
        }
    }

    public static Boolean isNewsItemFormOneContainsEmpty() {

        if (TextUtils.isEmpty(newsState.getNewsTitle())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getShortDescription())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getNewsCategory())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getNewsImageUrl())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getAuthor())) {
            return false;
        } else if (TextUtils.isEmpty(newsState.getDatePublished())) {
            return false;
        } else {
            return true;
        }
    }

    public static Boolean isNewsItemFormTwoContainsEmpty() {
        if (TextUtils.isEmpty(newsState.getNewsContent())) {
            return false;
        } else {
            return true;
        }
    }

    public static Boolean isBannerItemContainsEmpty(){
        if (TextUtils.isEmpty(bannerState.getBannerName())) {
            return true;
        } else if (TextUtils.isEmpty(bannerState.getCategoryName())){
            return true;
        } else if(TextUtils.isEmpty(bannerState.getBannerImageUrl())){
            return true;
        } else if (TextUtils.isEmpty(bannerState.getBannerPosition())){
            return true;
        } else {
            return false;
        }
    }
}
