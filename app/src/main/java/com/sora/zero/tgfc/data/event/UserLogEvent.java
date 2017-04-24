package com.sora.zero.tgfc.data.event;

/**
 * Created by zsy on 4/24/17.
 */

/**
 * an event for notifying user log in and out
 */
public class UserLogEvent {

    private boolean isLogged;

    public UserLogEvent(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }
}
