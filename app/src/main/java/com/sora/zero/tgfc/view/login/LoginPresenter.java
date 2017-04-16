package com.sora.zero.tgfc.view.login;

import com.sora.zero.tgfc.App;
import com.sora.zero.tgfc.data.api.model.User;
import com.sora.zero.tgfc.data.api.model.wrapper.LoginResultWrapper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zsy on 4/10/17.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;

    private Disposable mDisposable;

    @Override
    public void login(String username, String password) {
        mView.setLoadingIndicator(true);

        mDisposable = App.getAppComponent()
                .getTGFCService()
                .login(username, password)
                .map(s -> LoginResultWrapper.fromSource(s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        wrapper -> {
                            if(wrapper.isLoginSuccess()){
                                User user = App.getAppComponent().getUser();
                                user.init();
                                mView.showMessage("Welcome back " + username);
                                mView.finish();
                            }
                            else
                                mView.showMessage(wrapper.errorInfo);
                        }

                );

        mView.setLoadingIndicator(false);

    }

    @Override
    public void subscribe(){

    }

    @Override
    public void unsubscribe(){
        if(mDisposable != null)
            mDisposable.dispose();
    }

    @Override
    public void setView(LoginContract.View view) {
        mView = view;
    }
}
