
package com.jsyh.buyer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class CustomerModel implements Parcelable {

    @SerializedName("type")
    private String mType;
    @SerializedName("value")
    private String mValue;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mType);
        dest.writeString(this.mValue);
    }

    public CustomerModel() {
    }

    protected CustomerModel(Parcel in) {
        this.mType = in.readString();
        this.mValue = in.readString();
    }

    public static final Parcelable.Creator<CustomerModel> CREATOR = new Parcelable.Creator<CustomerModel>() {
        @Override
        public CustomerModel createFromParcel(Parcel source) {
            return new CustomerModel(source);
        }

        @Override
        public CustomerModel[] newArray(int size) {
            return new CustomerModel[size];
        }
    };
}
