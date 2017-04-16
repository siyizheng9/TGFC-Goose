package com.sora.zero.tgfc.utils;

import android.support.annotation.Nullable;
import android.text.Html;

import com.google.common.base.Optional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zsy on 3/18/17.
 */

public class StringUtils {

    static final String TWO_SPACES = "  ";

    //see http://stackoverflow.com/questions/37904739/html-fromhtml-deprecated-in-android-n
    @SuppressWarnings("deprecation")
    public static String fromHtml(String html) {
        String result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            result = Html.fromHtml(html).toString();
        }
        return result;
    }

    /**
     *Convert a String date to Date date
     * @param dateStr String contains date information with possible formats
     *                '17-04-05 10:57' and '2017-4-5 11:26'
     * @return Optional<Date>
     */
    public static Optional<Date > dateFromString(@Nullable String dateStr){
        if(dateStr == null)
            return Optional.absent();

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat format2 = new SimpleDateFormat("yy-MM-dd HH:mm");
        Date dateTime = null;

        try{
            if(dateStr.charAt(2) != '-')
                dateTime = format1.parse(dateStr);
            else
                dateTime = format2.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Optional.fromNullable(dateTime);

    }
}
