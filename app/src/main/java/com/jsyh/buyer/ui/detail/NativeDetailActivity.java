package com.jsyh.buyer.ui.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.ali.auth.third.ui.context.CallbackContext;
import com.jsyh.buyer.BaseActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.adapter.NativeRecyclerAdapter;
import com.jsyh.buyer.model.GoodsModel;
import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.ui.iview.CommonView;
import com.jsyh.buyer.ui.presenter.CommonPresnter;
import com.jsyh.buyer.utils.ActivityUtils;
import com.jsyh.buyer.utils.L;
import com.jsyh.buyer.utils.ResourcesUtil;
import com.momo.library.ShareCheckUtils;
import com.momo.library.share.ShareManger;
import com.momo.library.share.ShareType;
import com.momo.library.share.qq.TencetShare;
import com.momo.library.share.sina.SinaShare;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.MD5;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NativeDetailActivity extends BaseActivity implements CommonView,NativeRecyclerAdapter.OnItemClieck
        ,ShareManger.OnShareItemClickListener
        , IWeiboHandler.Response, IUiListener {



    @BindView(R.id.recycler)
    RecyclerView recycler;

    private String goodsUrl;
    private String voucherId;     //券id
    private String goodsClass;      //商品类别


    private GridLayoutManager gridLayoutManager;
    private NativeRecyclerAdapter mAdapter;


    private CommonPresnter mPresnter;

    private ShareManger shareManger;

    private String mSharePicUrl;
    private Bitmap mShareBitmap;
    private String mSharePicPath;
    private String goodsId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresnter = new CommonPresnter(this);

        Bundle extras = getIntent().getExtras();

        initRecyclerConfig();
        initHeadView((GoodsModel) extras.getParcelable("data"));


        if (!TextUtils.isEmpty(goodsClass)) {
            try {
                int anInt = Integer.parseInt(goodsClass);
                mPresnter.loadGoods(null,null,anInt,null,1,8,null,null,goodsId,1);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        initShare();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //handleWeiboResponse(intent, this);
        if (shareManger != null && shareManger.getSinaShare() != null) {
            ((SinaShare) shareManger.getSinaShare()).getWeiboShareAPI()
                    .handleWeiboResponse(intent, this);
        }

    }

    //登录须重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackContext.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (shareManger != null && shareManger.getSinaShare() != null) {
            ((SinaShare) shareManger.getSinaShare()).getSsoHandler()
                    .authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresnter.unsubscribe();
    }



    private void initHeadView(GoodsModel headData) {
        if (headData ==null) {
            return;
        }

        mAdapter.setHeadData(headData);

        goodsUrl = headData.getCouponsHref();
        goodsId = headData.getGoodsId();
        voucherId =headData.getCouponsId();
        goodsClass = headData.getGoodsClass();

        try {
            String goodsId = headData.getGoodsId();
            if (!TextUtils.isEmpty(goodsId)) {
                int i = Integer.parseInt(goodsId);
                mPresnter.sharePic(i);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private void initRecyclerConfig() {
        gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setAutoMeasureEnabled(true);

        recycler.setLayoutManager(gridLayoutManager);
        mAdapter = new NativeRecyclerAdapter();
        mAdapter.setmOnItemClieck(this);

        recycler.setAdapter(mAdapter);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = recycler.getAdapter().getItemViewType(position);
                switch (viewType) {
                    case 1:

                        return 2;

                    case 2:
                        return 1;
                }
                return 0;
            }
        });

        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                int spanSize = params.getSpanSize();
                int spanIndex = params.getSpanIndex();

                if (spanSize == 1) {

                    if (spanIndex == 0) {

                        outRect.left = (int) getDIP(R.dimen.goodsItemParentPadding);
                        outRect.top = (int) getDIP(R.dimen.goodsItemPadding);
                        outRect.right = (int) getDIP(R.dimen.goodsItemPadding);
                        outRect.bottom = (int) getDIP(R.dimen.goodsItemPadding);

                    } else {
                        outRect.left = (int) getDIP(R.dimen.goodsItemPadding);
                        outRect.top = (int) getDIP(R.dimen.goodsItemPadding);
                        outRect.right = (int) getDIP(R.dimen.goodsItemParentPadding);
                        outRect.bottom = (int) getDIP(R.dimen.goodsItemPadding);
                    }
                }

            }
        });

    }


    @Override
    protected int getRootView() {
        return R.layout.native_detail_activity;
    }

    @OnClick({R.id.back, R.id.share})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back:
                finish();
                break;
            case R.id.share:
                if (ResourcesUtil.getBooleanId(this, "has_share") > 0) {
                    if (getResources().getBoolean(ResourcesUtil.getBooleanId(this, "has_share"))) {

                        shareManger.showShare();
                    } else {
                        Toast.makeText(this, R.string.has_share_tips, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void initShare() {
        shareManger = new ShareManger()
                .initShareDialog(this);
        if (ShareCheckUtils.hasSinaConfig(this)) {
            shareManger.addSina();
        }
        if (ShareCheckUtils.hasWxConfig(this)) {
            shareManger.addWXSceneSession()
                    .addWXTimeLine();
        }
        if (ShareCheckUtils.hasTenceConfig(this)) {
            shareManger.addTencentQQ()
                    .addTencentQzone();
        }
        shareManger.setOnShareItemClickListener(this);

    }

//===========================view=============================
    @Override
    public void onLoadGoods(List<GoodsModel> model) {
        if (model != null) {
            mAdapter.setDatas(model);
        }
    }

    @Override
    public void onStartGoods() {
        mLoadDialog.show();
    }

    @Override
    public void onLoadGoodsErrorOrComplete() {
        mLoadDialog.dismiss();
    }

    @Override
    public void onLoadCategory(List<GoogsCategoryModel> model) {

    }

    @Override
    public void onLoadCategoryError() {

    }

    @Override
    public void onFindUnreadMsg(boolean msg) {

    }

    @Override
    public void onLoadSharePicUlr(String url) {
        mSharePicUrl = new String(url);

    }

    @Override
    public void onLoadShareBitmap( Bitmap bitmap) {
        final Bitmap tempBitmap = bitmap;
        new Thread(){
            @Override
            public void run() {
                if (tempBitmap != null) {

                    mShareBitmap = Bitmap.createBitmap(tempBitmap);
                    try {
                        String md5Url = MD5.hexdigest(mSharePicUrl);
                        String imagePath = getExternalCacheDir().getAbsolutePath();
                        File file = new File(imagePath + File.separator + md5Url + ".png");
                        mSharePicPath = file.getAbsolutePath();
                        if (file.exists()) {
                            return;
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        tempBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                        bos.flush();
                        bos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        }.start();

    }

    //================================share=======================
    @Override
    public void onShareItemClickListener(ShareType shareType) {
        shareManger.dismiss();
        if (mShareBitmap == null) {
            return;
        }
        if (shareType instanceof TencetShare) {
            ((TencetShare) shareType).share(this, mSharePicPath, mSharePicUrl,null,null, this);
        } else {

            shareType.share(this, mShareBitmap);
        }
    }

    //微信授权
    @Override
    public void onResponse(BaseResponse baseResponse) {
        if (baseResponse != null) {
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this,
                            "failed Error Message: " + baseResponse.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    //QQ分享回调

    @Override
    public void onComplete(Object o) {
        L.dd("tence onComplete");
    }

    @Override
    public void onError(UiError uiError) {
        L.dd(uiError.errorMessage);

    }

    @Override
    public void onCancel() {
        L.dd("tence onCancel()");
    }

    @Override
    public void onGoodsClickListener(Parcelable parcelable) {
        ActivityUtils.showDetailView(this, parcelable);
    }

    @Override
    public void onDetail(String iid, String imageId,String url) {
        ActivityUtils.showDetailView(this, imageId, iid, url,null,null);
    }


}
