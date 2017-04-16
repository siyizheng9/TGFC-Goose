package com.sora.zero.tgfc.view.login;

import com.sora.zero.tgfc.data.api.model.ForumThread;
import com.sora.zero.tgfc.view.base.BasePresenter;
import com.sora.zero.tgfc.view.base.BaseView;

/**
 * Created by zsy on 4/10/17.
 */

public class LoginContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMessage(String msg);

        boolean isActive();

        void finish();

    }

    interface Presenter extends BasePresenter {

        void login(String username, String password);

        void setView(View view);

    }
}
