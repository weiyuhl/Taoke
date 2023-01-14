
package com.jsyh.buyer.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GoogsCategoryModel {

    @SerializedName("class_id")
    private int mClassId;
    @SerializedName("img")
    private String mImg;
    @SerializedName("name")
    private String mName;
    @SerializedName("sort")
    private String mSort;
    @SerializedName("status")
    private String mStatus;

    public int getClassId() {
        return mClassId;
    }

    public void setClassId(int classId) {
        mClassId = classId;
    }

    public String getImg() {
        return mImg;
    }

    public void setImg(String img) {
        mImg = img;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSort() {
        return mSort;
    }

    public void setSort(String sort) {
        mSort = sort;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
