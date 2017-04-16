package com.sora.zero.tgfc.utils;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by zsy on 4/4/17.
 */

public class ViewUtils {

    public static void setForegroundColor(TextView textView, int color, int start, int end) {
        Spannable spannable = Spannable.Factory.getInstance().newSpannable(textView.getText());
        spannable.setSpan(new ForegroundColorSpan(color), start, end,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(spannable);
    }
}
