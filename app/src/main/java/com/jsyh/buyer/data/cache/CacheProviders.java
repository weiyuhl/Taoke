package com.jsyh.buyer.data.cache;

import com.jsyh.buyer.model.AdvertisingModel;
import com.jsyh.buyer.model.BaseModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

/**
 * Created by mo on 17-5-4.
 */

public interface CacheProviders {

    @LifeCache(duration = 30,timeUnit = TimeUnit.SECONDS)
    Observable<Reply<BaseModel<List<AdvertisingModel>>>> getCacheAdv(Observable<BaseModel<List<AdvertisingModel>>> observable);
}
