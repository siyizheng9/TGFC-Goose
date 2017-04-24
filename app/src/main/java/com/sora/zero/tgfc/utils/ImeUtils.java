package com.sora.zero.tgfc.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.os.ResultReceiver;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.lang.reflect.Method;

/**
 * Created by zsy on 4/22/17.
 */

public class ImeUtils {

    private ImeUtils() {

    }

    public static void showIme(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        // the public methods don't seem to work for me, so... reflection.
        try{
            Method showSoftInputUnchecked = InputMethodManager.class.getMethod(
                    "showSoftInputUnchecked", int.class, ResultReceiver.class);
            showSoftInputUnchecked.setAccessible(true);
            showSoftInputUnchecked.invoke(imm, 0, null);
        } catch (Exception e) {

        }
    }

    public static void hideIme(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Backports {@link TextView#setShowSoftInputOnFocus} to API 20 and below.
     *
     * @see TextView#setShowSoftInputOnFocus(boolean)
     */
    public static void setShowSoftInputOnFocus(TextView textView, Boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setShowSoftInputOnFocus(show);
        } else {
            try {
                Method method;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    method = TextView.class.getMethod("setShowSoftInputOnFocus", boolean.class);
                } else {
                    method = TextView.class.getMethod("setSoftInputShownOnFocus", boolean.class);
                }
                method.setAccessible(true);
                method.invoke(textView, show);
            } catch (Exception e) {
                // multi-catch with those reflection exceptions requires API level 19
                // so we use Exception instead of multi-catch
                throw new RuntimeException("Failed to invoke TextView#setShowSoftInputOnFocus(boolean).", e);
            }
        }
    }
}
