package com.jsyh.buyer.ui.iview;

import com.ali.auth.third.login.callback.LogoutCallback;
import com.jsyh.buyer.base.BaseView;
import com.jsyh.buyer.model.BaseModel;
import com.jsyh.buyer.model.PersonModel;
import com.jsyh.buyer.model.ShareAppModel;

/**
 *
 * Created by mo on 17-4-19.
 */

public interface MeView extends BaseView {


    void onLoginSuccess(PersonModel model);

    void onLoginFailure(int code, String msg);

    void onLogoutSuccess();

    void onLogoutFailure(int code, String msg);

    void onShareData(BaseModel<ShareAppModel> model);


}
