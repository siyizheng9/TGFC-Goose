package com.sora.zero.tgfc.utils;

import android.util.Log;

import com.sora.zero.tgfc.BuildConfig;

/**
 * Created by zsy on 3/18/17.
 * Forked from https://github.com/ykrank/S1-Next
 */

public class L {
    private static final String LOG_TAG = "TGFCApp";

    public static boolean showLog() {
        return BuildConfig.DEBUG;
    }
    public static void d(String msg) {
        d(LOG_TAG, msg);
    }

    public static void i(String msg) {
        i(LOG_TAG, msg);
    }

    public static void e(String msg) {
        e(LOG_TAG, msg);
    }

    public static void d(String msg, Throwable tr) {
        d(LOG_TAG, msg, tr);
    }

    public static void i(String msg, Throwable tr) {
        i(LOG_TAG, msg, tr);
    }

    public static void d(String tag, String msg) {
        if (showLog()) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (showLog()) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (showLog()) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (showLog()) {
            Log.e(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (showLog()) {
            Log.d(tag, msg, tr);
        }
    }


    public static void i(String tag, String msg, Throwable tr) {
        if (showLog()) {
            Log.i(tag, msg, tr);
        }
    }

    public static void report(Throwable tr) {
        report(tr, Log.WARN);
    }

    public static void report(Throwable tr, int severity) {

    }

}
