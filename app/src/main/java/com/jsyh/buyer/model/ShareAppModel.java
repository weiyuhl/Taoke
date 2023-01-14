package com.jsyh.buyer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mo on 17-4-28.
 */

public class ShareAppModel implements Parcelable {


    @SerializedName("kefu")
    private List<CustomerModel> kefu;

    @SerializedName("share_address")
    private String shareAddress;

    @SerializedName("share_introduce")
    private String shareIntroduce;      //描述

    @SerializedName("share_title")
    private String shareTitle;      //标题



    public List<CustomerModel> getKefu() {
        return kefu;
    }

    public void setKefu(List<CustomerModel> kefu) {
        this.kefu = kefu;
    }

    public String getShareAddress() {
        return shareAddress;
    }

    public void setShareAddress(String shareAddress) {
        this.shareAddress = shareAddress;
    }



    public ShareAppModel() {
    }

    public String getShareIntroduce() {
        return shareIntroduce;
    }

    public void setShareIntroduce(String shareIntroduce) {
        this.shareIntroduce = shareIntroduce;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.kefu);
        dest.writeString(this.shareAddress);
        dest.writeString(this.shareIntroduce);
        dest.writeString(this.shareTitle);
    }

    protected ShareAppModel(Parcel in) {
        this.kefu = in.createTypedArrayList(CustomerModel.CREATOR);
        this.shareAddress = in.readString();
        this.shareIntroduce = in.readString();
        this.shareTitle = in.readString();
    }

    public static final Parcelable.Creator<ShareAppModel> CREATOR = new Parcelable.Creator<ShareAppModel>() {
        @Override
        public ShareAppModel createFromParcel(Parcel source) {
            return new ShareAppModel(source);
        }

        @Override
        public ShareAppModel[] newArray(int size) {
            return new ShareAppModel[size];
        }
    };
}
