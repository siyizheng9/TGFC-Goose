package com.sora.zero.tgfc.data.api.model.wrapper;

import android.support.annotation.Nullable;

import com.sora.zero.tgfc.data.api.model.ThreadType;
import com.sora.zero.tgfc.utils.L;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zsy on 4/24/17.
 */

public class ThreadTypeWrapper extends BaseWrapper {

    private List<ThreadType> mThreadTypes;

    @Nullable
    public static ThreadTypeWrapper fromSource(String source) {

        source = StringEscapeUtils.unescapeHtml4(source);

        if(source == null) {
            return null;
        }

        ThreadTypeWrapper wrapper = new ThreadTypeWrapper();
        List<ThreadType> mTypes = new ArrayList<>();

        Pattern pattern = Pattern.compile("submit\\(\\);\\}.>(.*)<\\/se");
        Matcher matcher = pattern.matcher(source);

        if(matcher.find()) {
            // example: <option value="290">漫画</option>
            Document doc = Jsoup.parse(matcher.group(1));
            Elements options = doc.select("option");
            for (Element option : options) {
                ThreadType threadType = new ThreadType(option.attr("value"), option.html());
                mTypes.add(threadType);
                //L.d("ThreadTypeWrapper", threadType.toString());
            }

            wrapper.setThreadTypes(mTypes);

        } else {

            wrapper.setErrorInfo("Network error", ERROR_TYPE_OTHERS);
            return wrapper;
        }
        return wrapper;
    }

    public List<ThreadType> getThreadTypes() {
        return mThreadTypes;
    }

    public void setThreadTypes(List<ThreadType> threadTypes) {
        mThreadTypes = threadTypes;
    }
}
