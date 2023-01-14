
package com.jsyh.buyer.model;

import com.google.gson.annotations.SerializedName;

/**
 * 广告
 */
public class AdvertisingModel {

    @SerializedName("pic_url")
    private String mPicUrl;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("time")
    private String time;

    @SerializedName("content")
    private String content;

    @SerializedName("goods_id")
    private String goodsId;

    public String getPicUrl() {
        return mPicUrl;
    }

    public void setPicUrl(String picUrl) {
        mPicUrl = picUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AdvertisingModel{" +
                "mPicUrl='" + mPicUrl + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}
