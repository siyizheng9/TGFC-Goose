package com.sora.zero.tgfc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zsy on 3/18/17.
 */

/**
 * Forked from
 */
public class NetworkUtils {
    public static int getTidFromURL(String url)
    {
        Pattern pattern = Pattern.compile("index.php\\?action=thread&tid=(\\d+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find())
        {
            return Integer.parseInt(matcher.group(1));
        }
        else
        {
            return -1;
        }
    }
}
