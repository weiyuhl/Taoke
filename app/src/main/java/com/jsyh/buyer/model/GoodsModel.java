
package com.jsyh.buyer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GoodsModel implements Parcelable {

    private int viewType;

    @SerializedName("coupons_condition")
    private String mCouponsCondition;
    @SerializedName("coupons_end")
    private String mCouponsEnd;
    @SerializedName("coupons_href")
    private String mCouponsHref;
    @SerializedName("coupons_id")
    private String mCouponsId;
    @SerializedName("coupons_price")
    private String mCouponsPrice;
    @SerializedName("coupons_receive")
    private String mCouponsReceive;
    @SerializedName("coupons_surplus")
    private String mCouponsSurplus;
    @SerializedName("create_time")
    private String mCreateTime;
    @SerializedName("goods_class")
    private String mGoodsClass;
    @SerializedName("goods_id")
    private String mGoodsId;
    @SerializedName("iid")
    private String mIid;
    @SerializedName("img_url")
    private String mImgUrl;
    @SerializedName("is_recommend")
    private String mIsRecommend;
    @SerializedName("istmall")
    private String mIstmall;
    @SerializedName("item_url")
    private String mItemUrl;
    @SerializedName("org_price")
    private String mOrgPrice;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("volume")
    private String mVolume;

    public String getCouponsCondition() {
        return mCouponsCondition;
    }

    public void setCouponsCondition(String couponsCondition) {
        mCouponsCondition = couponsCondition;
    }

    public String getCouponsEnd() {
        return mCouponsEnd;
    }

    public void setCouponsEnd(String couponsEnd) {
        mCouponsEnd = couponsEnd;
    }

    public String getCouponsHref() {
        return mCouponsHref;
    }

    public void setCouponsHref(String couponsHref) {
        mCouponsHref = couponsHref;
    }

    public String getCouponsId() {
        return mCouponsId;
    }

    public void setCouponsId(String couponsId) {
        mCouponsId = couponsId;
    }

    public String getCouponsPrice() {
        return mCouponsPrice;
    }

    public void setCouponsPrice(String couponsPrice) {
        mCouponsPrice = couponsPrice;
    }

    public String getCouponsReceive() {
        return mCouponsReceive;
    }

    public void setCouponsReceive(String couponsReceive) {
        mCouponsReceive = couponsReceive;
    }

    public String getCouponsSurplus() {
        return mCouponsSurplus;
    }

    public void setCouponsSurplus(String couponsSurplus) {
        mCouponsSurplus = couponsSurplus;
    }

    public String getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(String createTime) {
        mCreateTime = createTime;
    }

    public String getGoodsClass() {
        return mGoodsClass;
    }

    public void setGoodsClass(String goodsClass) {
        mGoodsClass = goodsClass;
    }

    public String getGoodsId() {
        return mGoodsId;
    }

    public void setGoodsId(String goodsId) {
        mGoodsId = goodsId;
    }

    public String getIid() {
        return mIid;
    }

    public void setIid(String iid) {
        mIid = iid;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    public String getIsRecommend() {
        return mIsRecommend;
    }

    public void setIsRecommend(String isRecommend) {
        mIsRecommend = isRecommend;
    }

    public String getIstmall() {
        return mIstmall;
    }

    public void setIstmall(String istmall) {
        mIstmall = istmall;
    }

    public String getItemUrl() {
        return mItemUrl;
    }

    public void setItemUrl(String itemUrl) {
        mItemUrl = itemUrl;
    }

    public String getOrgPrice() {
        return mOrgPrice;
    }

    public void setOrgPrice(String orgPrice) {
        mOrgPrice = orgPrice;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getVolume() {
        return mVolume;
    }

    public void setVolume(String volume) {
        mVolume = volume;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.viewType);
        dest.writeString(this.mCouponsCondition);
        dest.writeString(this.mCouponsEnd);
        dest.writeString(this.mCouponsHref);
        dest.writeString(this.mCouponsId);
        dest.writeString(this.mCouponsPrice);
        dest.writeString(this.mCouponsReceive);
        dest.writeString(this.mCouponsSurplus);
        dest.writeString(this.mCreateTime);
        dest.writeString(this.mGoodsClass);
        dest.writeString(this.mGoodsId);
        dest.writeString(this.mIid);
        dest.writeString(this.mImgUrl);
        dest.writeString(this.mIsRecommend);
        dest.writeString(this.mIstmall);
        dest.writeString(this.mItemUrl);
        dest.writeString(this.mOrgPrice);
        dest.writeString(this.mPrice);
        dest.writeString(this.mStatus);
        dest.writeString(this.mTitle);
        dest.writeString(this.mVolume);
    }

    public GoodsModel() {
    }

    protected GoodsModel(Parcel in) {
        this.viewType = in.readInt();
        this.mCouponsCondition = in.readString();
        this.mCouponsEnd = in.readString();
        this.mCouponsHref = in.readString();
        this.mCouponsId = in.readString();
        this.mCouponsPrice = in.readString();
        this.mCouponsReceive = in.readString();
        this.mCouponsSurplus = in.readString();
        this.mCreateTime = in.readString();
        this.mGoodsClass = in.readString();
        this.mGoodsId = in.readString();
        this.mIid = in.readString();
        this.mImgUrl = in.readString();
        this.mIsRecommend = in.readString();
        this.mIstmall = in.readString();
        this.mItemUrl = in.readString();
        this.mOrgPrice = in.readString();
        this.mPrice = in.readString();
        this.mStatus = in.readString();
        this.mTitle = in.readString();
        this.mVolume = in.readString();
    }

    public static final Parcelable.Creator<GoodsModel> CREATOR = new Parcelable.Creator<GoodsModel>() {
        @Override
        public GoodsModel createFromParcel(Parcel source) {
            return new GoodsModel(source);
        }

        @Override
        public GoodsModel[] newArray(int size) {
            return new GoodsModel[size];
        }
    };
}
