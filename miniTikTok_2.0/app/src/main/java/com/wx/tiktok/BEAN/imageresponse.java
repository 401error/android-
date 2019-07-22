package com.wx.tiktok.BEAN;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class imageresponse {

    @SerializedName("feeds") private List<Cat> feeds;
    private boolean success;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setFeeds(List<Cat> feeds) {
        this.feeds = feeds;
    }

    public List<Cat> getFeeds() {
        return feeds;
    }
}
