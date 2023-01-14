package com.jsyh.buyer.model;

import java.util.List;

/**
 * Created by mo on 17-4-13.
 */

public class HomeModel {


    public HomeModel(List<AdvertisingModel> advertisingModels, List<GoogsCategoryModel> goods) {
        this.advertisingModels = advertisingModels;
        this.goods = goods;
    }

    private List<AdvertisingModel> advertisingModels;

    private List<GoogsCategoryModel> goods;

    public List<AdvertisingModel> getAdvertisingModels() {
        return advertisingModels;
    }

    public void setAdvertisingModels(List<AdvertisingModel> advertisingModels) {
        this.advertisingModels = advertisingModels;
    }

    public List<GoogsCategoryModel> getGoods() {
        return goods;
    }

    public void setGoods(List<GoogsCategoryModel> goods) {
        this.goods = goods;
    }
}
