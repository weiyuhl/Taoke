package com.jsyh.buyer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mo on 17-4-13.
 */

public class GoodsListModel {


    @SerializedName("total_results")
    private String totalResults;

    @SerializedName("goods")
    private List<GoodsModel> goods;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<GoodsModel> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsModel> goods) {
        this.goods = goods;
    }
}
