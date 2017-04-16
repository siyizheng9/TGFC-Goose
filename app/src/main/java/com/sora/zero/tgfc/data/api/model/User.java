package com.sora.zero.tgfc.data.api.model;

import android.databinding.BaseObservable;

import com.sora.zero.tgfc.App;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zsy on 4/10/17.
 */

public class User extends BaseObservable {

    private String uid;

    private String name;

    private boolean logged;

    private String avatarUrl;

    public void init() {
        App.getAppComponent().getTGFCService()
                .getAccountInfo()
                .map(s -> UserInfo.fromSource(s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        optional -> {
                            if(optional.isPresent()){
                                UserInfo userInfo = optional.get();
                                uid = userInfo.getUid();
                                name = userInfo.getUserName();
                                avatarUrl = userInfo.getAvatarUrl();
                                logged = true;
                                notifyChange();
                            } else {
                                logged = false;
                            }
                        }

                );

    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
        notifyChange();
    }

}
