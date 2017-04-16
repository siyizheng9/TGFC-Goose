package com.sora.zero.tgfc.data.api.model.wrapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sora.zero.tgfc.data.api.model.ForumThread;
import com.sora.zero.tgfc.utils.NetworkUtils;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zsy on 3/18/17.
 */

/**
 * this class represents a thread list
 */
public class ForumThreadListWrapper extends BaseWrapper{
    @Nullable private int page;
    @Nullable private int maxPage;
    @Nullable private List<ForumThread> threads;

    /**
     * create a ForumThreadWrapper instance from source html.
     * @param source
     * @return
     */
    public static ForumThreadListWrapper fromSource(String source) {
        source = StringEscapeUtils.unescapeHtml4(source);
        ForumThreadListWrapper wrapper = new ForumThreadListWrapper();
        List<ForumThread> threads = new ArrayList<>();

        if (source.contains("<div>无权访问本版块</div>"))
        {
            wrapper.setErrorInfo("无权访问本版块", BaseWrapper.ERROR_TYPE_NOT_AUTHORIZED);
            return wrapper;
        }
        else if (source.contains("<title>TGFC 手机版</title>"))
        {
            wrapper.setErrorInfo("版面不存在", BaseWrapper.ERROR_TYPE_ARGUMENT_ERROR);
            return wrapper;
        }


        Pattern pattern = Pattern.compile("<span class=\"title\">(<b>)?<a href=\"([^\"]+)\">([^<]+)<\\/a>(<\\/b>)?<\\/span>(?:<span class=\"paging\">.*<\\/span>)?<br \\/>\\s*<span class=\"author\">\\[(?:<b>)?([^\\/]+)(?:<\\/b>)?\\/(\\d+)\\/(\\d+)\\/(?:<b>)?([^\\/]+)(?:<\\/b>)?]<\\/span>");
        Matcher matcher = pattern.matcher(source);

        while(matcher.find()) {
            ForumThread thread = new ForumThread();
            String topFlag = matcher.group(1);
            String itemURL = matcher.group(2);
            String title = matcher.group(3);
            String posterName = matcher.group(5);
            String replyCount = matcher.group(6);
            String readCount = matcher.group(7);
            String replierName = matcher.group(8);
            thread.setTopFlag(topFlag == null);
            thread.setTid(NetworkUtils.getTidFromURL(itemURL));
            thread.setTitle(title);
            thread.setAuthor(posterName);
            thread.setLastReplierName(replierName);
            thread.setReadCount(Integer.parseInt(readCount));
            thread.setReplyCount(Integer.parseInt(replyCount));
            threads.add(thread);
            //L.d(thread.toString());
        }

        wrapper.setThreads(threads);

        return wrapper;
    }

    public void setThreads(@NonNull List<ForumThread> threads) {
        this.threads = threads;
    }

    @Nullable
    public List<ForumThread> getThreads() {
        return threads;
    }
}
