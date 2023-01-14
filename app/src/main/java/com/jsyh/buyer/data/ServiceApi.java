package com.jsyh.buyer.data;

import com.jsyh.buyer.model.AdvertisingModel;
import com.jsyh.buyer.model.BaseModel;
import com.jsyh.buyer.model.GoodsListModel;
import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.model.ShareAppModel;
import com.jsyh.buyer.model.SharePicModel;
import com.jsyh.buyer.model.UpdataModel;
import com.sina.weibo.sdk.api.share.Base;

import java.util.List;

import io.reactivex.Observable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;


/**
 * Created by mo on 17-4-7.
 */

public interface ServiceApi {

    //广告
    @POST("index.php?g=port&m=api&a=ad")
    Observable<BaseModel<List<AdvertisingModel>>> getAdv(@Query("type") String type);

    //类型列表
    @POST("index.php?g=port&m=api&a=goods_class")
    Observable<BaseModel<List<GoogsCategoryModel>>> getGoodsClass(@Query("recommend")Integer recommend);

    /**
     *
     * @param startPrice 价格下限
     * @param endPrice  价格上限
     * @param goodsClass 分类
     * @param isRecommend 推荐商品，1为推荐
     * @param page  第几页，默认1
     * @param pageCount 每页商品数量，默认20
     * @param keyword  商品关键词
     * @param order 排序
     *              销量排序(volume_desc,volume_asc)
                    优惠券领取量(receive_desc,receive_asc)
                    价格(price_desc,price_asc)
     *@param goodsId 商品id
     *@param like 推荐 1
     * @return
     */
    @POST("index.php?g=port&m=api&a=goods_list")
    Observable<BaseModel<GoodsListModel>> getGoods(@Query("start_price") Double startPrice,
                                                   @Query("end_price") Double endPrice,
                                                   @Query("goods_class") Integer goodsClass,
                                                   @Query("is_recommend") Integer isRecommend,
                                                   @Query("page") Integer page,
                                                   @Query("page_count") Integer pageCount,
                                                   @Query("keyword") String keyword,
                                                   @Query("order") String order,
                                                   @Query("goods_id")String goodsId,
                                                    @Query("like")Integer like);


    /**
     * 分享图片
     * @param goodsId
     * @return
     */
    @POST("index.php?g=port&m=api&a=get_share_pic")
    Observable<BaseModel<SharePicModel>> getSharePicUrl(@Query("goods_id") Integer goodsId);

    @GET
    Observable<ResponseBody> getSharePicByte(@Url String url);


    /**
     * 意见反馈
     * @param description
     * @param version
     * @return
     */
    @POST("index.php?g=port&m=api&a=feed_back")
    Observable<BaseModel> feedback(@Query("description") String description, @Query("user_name") String version);

    //getUpdateInfo(@Url String url, @Field("appid") String appid, @Field("version") String version,@Field("mainurl")String mainurl);//获取app更新信息
    @POST
    Observable<BaseModel<UpdataModel>> updataApp(@Url String downloadUrl,@Query("appid") String appid, @Query("version") String version);


    @GET
    Call<ResponseBody> getApk(@Url String url);

    @GET
    Call<ResponseBody> dowloadFile(@Url String url);


    @POST("index.php?g=port&m=api&a=setting")
    Observable<BaseModel<ShareAppModel>> getShareData();



}
