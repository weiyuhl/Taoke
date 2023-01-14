package com.jsyh.buyer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mo on 17-4-26.
 *
 * 升级数据
 */

public class UpdataModel {




    @SerializedName("close")
    private String mClose;
    @SerializedName("force")
    private String mForce;
    @SerializedName("name")
    private String mName;
    @SerializedName("path")
    private String mPath;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("v_code")
    private String mVCode;
    @SerializedName("v_no")
    private Long mVNo;

    @SerializedName("site")
    private String site;

    @SerializedName("sign")
    private String sign;

    public String getClose() {
        return mClose;
    }

    public void setClose(String close) {
        mClose = close;
    }

    public String getForce() {
        return mForce;
    }

    public void setForce(String force) {
        mForce = force;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getVCode() {
        return mVCode;
    }

    public void setVCode(String vCode) {
        mVCode = vCode;
    }

    public Long getVNo() {
        return mVNo;
    }

    public void setVNo(Long vNo) {
        mVNo = vNo;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
