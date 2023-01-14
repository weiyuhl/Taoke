package com.momo.library.share;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;

import com.momo.library.R;
import com.momo.library.ShareDialog;
import com.momo.library.ShareModel;
import com.momo.library.share.qq.TencenQ;
import com.momo.library.share.qq.TencetQzone;
import com.momo.library.share.sina.SinaShare;
import com.momo.library.share.wx.SceneSessionShare;
import com.momo.library.share.wx.WXTimeLine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on 17-4-20.
 */

public class ShareManger {


    private List<ShareModel> shares;


    private ShareDialog shareDialog;


   public ShareType getSinaShare() {
        for (ShareModel model : shares) {
            if (model.getShareType() instanceof SinaShare) {
                return model.getShareType();
            }

        }
        return null;
    }
    private Activity  activity;

    public ShareManger() {


    }

    public ShareManger initShareDialog(Activity activity) {
        if (activity == null) {
            new Throwable("activity is null");
        }

        this.activity = activity;
        shares = new ArrayList<>();

        shareDialog = new ShareDialog(this.activity);



        return this;
    }




    public ShareManger addSina() {

        ShareModel sina = new ShareModel(activity.getString(R.string.sina),
                ContextCompat.getDrawable(activity, R.drawable.share_sina));

        sina.setShareType(new SinaShare(activity));
        removeRepeat(sina);

        shares.add(sina);
        return this;

    }

    public ShareManger addWXTimeLine() {
        ShareModel wx = new ShareModel(activity.getString(R.string.timeLine),
                ContextCompat.getDrawable(activity, R.drawable.share_wechat_timeline));
        wx.setShareType(new WXTimeLine(activity));
        removeRepeat(wx);
        shares.add(wx);
        return this;
    }

    public ShareManger addWXSceneSession() {
        ShareModel wx = new ShareModel(activity.getString(R.string.sceneSession),
                ContextCompat.getDrawable(activity, R.drawable.share_wechat));
        wx.setShareType(new SceneSessionShare(activity));

        removeRepeat(wx);

        shares.add(wx);
        return this;

    }

    public ShareManger addTencentQQ() {
        ShareModel tencent = new ShareModel(activity.getString(R.string.tencetQQ),
                ContextCompat.getDrawable(activity, R.drawable.share_qq));
        tencent.setShareType(new TencenQ(activity));
        removeRepeat(tencent);
        shares.add(tencent);
        return this;

    }

    public ShareManger addTencentQzone() {
        ShareModel tencent = new ShareModel(activity.getString(R.string.tencetQzone),
                ContextCompat.getDrawable(activity, R.drawable.share_qzone));
        tencent.setShareType(new TencetQzone(activity));
        removeRepeat(tencent);
        shares.add(tencent);
        return this;

    }

    private void removeRepeat(ShareModel model) {
        try {
            for (int i = 0; i < shares.size(); i++) {
                if (model.getName().equals(shares.get(i).getName())) {
                    shares.remove(i);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showShare() {
        if (shares.isEmpty())return;


        shareDialog.setShares(shares);

        shareDialog.show();
    }

    public void setOnShareItemClickListener(ShareManger.OnShareItemClickListener onShareItemClickListener) {
        if (shareDialog == null) {
            new Throwable("ShareDialog no Instantiation");
        }
        shareDialog.setOnShareItemClickListener(onShareItemClickListener);
    }
    public interface OnShareItemClickListener{
        void onShareItemClickListener(ShareType shareType);
    }


    public void dismiss() {

        shareDialog.dismiss();

    }

}
