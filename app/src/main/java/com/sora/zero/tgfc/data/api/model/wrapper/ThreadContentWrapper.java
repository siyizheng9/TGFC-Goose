package com.sora.zero.tgfc.data.api.model.wrapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sora.zero.tgfc.data.api.APIURL;
import com.sora.zero.tgfc.data.api.model.ThreadContentItem;
import com.sora.zero.tgfc.utils.HtmlUtils;

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
 * Created by zsy on 3/22/17.
 */

/**
 * this class represents content of a thread
 */
public class ThreadContentWrapper extends BaseWrapper{

    private int tid;
    private int fid;
    private int totalPageCount;
    private int currentPage;
    private int totalReplyCount;
    private boolean isClosed;
    @Nullable private String title;
    @Nullable private List<ThreadContentItem> replyList;
    @Nullable private List<String>  imgURLList;

    /**
     * create a ThreadContentWrapper instance from source html and return it
     * tid and page are used for rebuild base url for Jsoup-parsing.
     * @param source source html
     * @param tid thread id
     * @param page page number of thread content
     * @return
     */
    public static ThreadContentWrapper fromSource(@NonNull String source, int tid, int page){
        String url = APIURL.WAP_VIEW_CONTENT_URL + tid + "&page=" + page;
        source = StringEscapeUtils.unescapeHtml4(source);
        List<ThreadContentItem> replyList = new ArrayList<ThreadContentItem>();
        ThreadContentWrapper wrapper = new ThreadContentWrapper();
        wrapper.replyList = replyList;

        if (source.contains("<div>指定主题不存在</div>"))
        {
            wrapper.setErrorInfo("指定的主题不存在", ERROR_TYPE_ARGUMENT_ERROR);
            return wrapper;
        }
        else if (source.contains("<div>无权查看本主题</div>"))
        {
            wrapper.setErrorInfo("无权查看本主题", ERROR_TYPE_NOT_AUTHORIZED);
            return wrapper;
        }
        else
        {
            ThreadContentWrapper.generalContentParser(source, wrapper, tid);
            ThreadContentWrapper.contentListParser(source, wrapper, url);

        }

        return wrapper;

    }



    /**
     * forked from https://github.com/cfan8/TGFC
     * extract title, currentPage, totalPageCount, totalReplyCount, fid and
     * isClosed from html, then feed them to the ThreadContentWrapper instance.
     * @param html source html
     * @param wrapper ThreadContentWrapper
     * @param tid   thread id
     */
    private static void generalContentParser(@NonNull String html, @NonNull ThreadContentWrapper wrapper, int tid) {
        wrapper.setTid(tid);
        Pattern titlePattern = Pattern.compile("<title>(.+)-TGFC 手机版<\\/title>");
        Matcher titleMatcher = titlePattern.matcher(html);
        if (titleMatcher.find())
        {
            wrapper.title = titleMatcher.group(1);
        }
        Pattern pageInfoPattern = Pattern.compile("\\((\\d+)\\/(\\d+)页\\)<\\/span>");
        Matcher pageInfoMatcher = pageInfoPattern.matcher(html);
        if (pageInfoMatcher.find())
        {
            wrapper.currentPage = Integer.parseInt(pageInfoMatcher.group(1));
            wrapper.totalPageCount = Integer.parseInt(pageInfoMatcher.group(2));
        }
        if (html.contains("[此主题已关闭]"))
        {
            wrapper.isClosed = true;
        }
        Pattern replyCountPattern = Pattern.compile("<b>回复列表 (\\d+)<\\/b>");
        Matcher replyCountMatcher = replyCountPattern.matcher(html);
        if (replyCountMatcher.find())
        {
            wrapper.totalReplyCount = Integer.parseInt(replyCountMatcher.group(1));
        }
        else
        {
            wrapper.totalReplyCount = 1;
        }
        Pattern fidPattern = Pattern.compile("fid=(\\d+)");
        Matcher fidMatcher = fidPattern.matcher(html);
        if (fidMatcher.find())
        {
            wrapper.fid = Integer.parseInt(fidMatcher.group(1));
        }
    }

    /**
     * Forked from https://github.com/cfan8/TGFC
     * extract main post and replies and add them to {@link ThreadContentWrapper}'s replyList.
     * extract all image urls from source html and set them to {@link ThreadContentWrapper}'s imgURLList.
     * imgURLList will be used for pre-loading before loading thread content
     * @param html source html
     * @param wrapper target wrapper
     * @param baseUri for Jsoup
     */
    private static void contentListParser(@NonNull String html,
            @NonNull ThreadContentWrapper wrapper, @NonNull String baseUri) {

        List<ThreadContentItem> dataList = wrapper.replyList;
        Document htmlDoc = Jsoup.parse(html, baseUri);
        Elements lightMessageEmelents = htmlDoc.select(".lightmessage");
        lightMessageEmelents.remove();
        Elements messageElements = htmlDoc.select(".message");
        Elements infobarElements = htmlDoc.select(".infobar");
        int messageStart = 0;
        String posterName = "";
        Pattern mainPostPattern = Pattern.compile("标题:<b>(.+)<\\/b><br \\/>时间:(.+)<br \\/>作者:<a href=\".+uid=(\\d+).*\">(?:<b>)?(.+?)(<\\/b>)?<\\/a>");
        Matcher mainPostMatcher = mainPostPattern.matcher(html);
        Pattern urlReplacePattern = Pattern.compile("<a\\s*.*?href=\"(.*?)\"\\s*.*?>.*?\\s\\.\\.\\.\\s.*?<\\/a>");

        if (mainPostMatcher.find())
        {
            messageStart++;
            ThreadContentItem itemData = new ThreadContentItem();
            itemData.setFloorNum(1);
            itemData.setPostTime(mainPostMatcher.group(2));
            itemData.setPosterUID(Integer.parseInt(mainPostMatcher.group(3)));
            itemData.setPosterName(mainPostMatcher.group(4));

            posterName = itemData.getPosterName();
            itemData.setPosterName(itemData.getPosterName() + " (楼主)");

            itemData.setCanEdit(mainPostMatcher.group(5) != null);
            Pattern ratingPattern = Pattern.compile("评分记录\\(.+?=(\\d+)\\)");
            Matcher ratingMatcher = ratingPattern.matcher(html);
            if (ratingMatcher.find())
            {
                itemData.setRatings(Integer.parseInt(ratingMatcher.group(1)));
            }
            Pattern pidPattern = Pattern.compile("作者:<a href=\".*?pid=(\\d+)[^\\>]*?>");
            Matcher pidMatcher = pidPattern.matcher(html);
            if (pidMatcher.find())
            {
                itemData.setPid(Integer.parseInt(pidMatcher.group(1)));
            }
            itemData.setMainText(messageElements.get(0).html());
            extractPlatform(itemData);
            dataList.add(itemData);
        }

        for (int i = messageStart, j = 0; i < messageElements.size(); i++, j++)
        {
            ThreadContentItem itemData = new ThreadContentItem();
            Element msgElement = messageElements.get(i);
            Element barElement = infobarElements.get(j);
            String infoString = StringEscapeUtils.unescapeHtml4(barElement.html());
            Pattern barPattern = Pattern.compile("<a href=\".*?pid=(\\d+).*?>#(\\d+)[\\s\\S]*?<a href=\".*?uid=(\\d+).*?>(?:<b>)?(.+?)(?:<\\/b>)?<\\/a>[\\s\\S]*?(?:骚\\((\\d+)\\)[\\s\\S]*?)?<span class=\"nf\">(?:<font \\S*>)? (?:<b>)?(.*?)(<\\/b>)?(?:<\\/font>)?<\\/span>");
            Matcher barMatcher = barPattern.matcher(infoString);
            if (barMatcher.find())
            {
                itemData.setPid(Integer.parseInt(barMatcher.group(1)));
                itemData.setFloorNum(Integer.parseInt(barMatcher.group(2)));
                itemData.setPosterUID(Integer.parseInt(barMatcher.group(3)));
                itemData.setPosterName(barMatcher.group(4));

                if (posterName.equals(itemData.getPosterName())){
                    itemData.setPosterName(itemData.getPosterName() + " (楼主)");
                }

                if (barMatcher.group(5) != null)
                {
                    itemData.setRatings(Integer.parseInt(barMatcher.group(5)));
                }
                itemData.setPostTime(barMatcher.group(6));
                itemData.setCanEdit(barMatcher.group(7) != null);
            }
            Elements quotedElements = msgElement.select(".quote-bd");
            if (quotedElements.size() > 0)
            {
                String quoteString = quotedElements.get(0).html();
                String divider = "<br>";
                int t = quoteString.indexOf(divider);
                if (t != -1)
                {
                    itemData.setQuotedInfo(quoteString.substring(0, t));
                    itemData.setQuotedInfo(HtmlUtils.cleanQuote(itemData.getQuotedInfo()));
                    itemData.setQuotedText(quoteString.substring(t + divider.length()));
                    itemData.setQuotedText(HtmlUtils.cleanText(itemData.getQuotedText()));
                }
                msgElement.select(".ui-topic-content").remove();
            }
            itemData.setMainText(msgElement.html());
            extractPlatform(itemData);
            dataList.add(itemData);
        }
        Pattern imgURLPattern = Pattern.compile("<img\\s*[^>]*?src\\s*=\\s*['\\\"]([^'\\\"]*?)['\\\"][^>]*?\\s*\\/?>");
        List<String> imgURLList = new ArrayList<String>();
        for (int i = 0; i < dataList.size(); i++)
        {
            ThreadContentItem itemData = dataList.get(i);
//			itemData.mainText = Jsoup.clean(itemData.mainText, Whitelist.basicWithImages());
            Matcher urlReplaceMatcher = urlReplacePattern.matcher(itemData.getMainText());
//			Log.w("Matcher", String.valueOf(urlReplaceMatcher.find()));
            itemData.setMainText(urlReplaceMatcher.replaceAll("<a href=\"$1\">$1</a>"));
        }
        for (int i = 0; i < dataList.size(); i++)
        {
            String itemHTML = dataList.get(i).getMainText();
            Matcher imgURLMatcher = imgURLPattern.matcher(itemHTML);
            while (imgURLMatcher.find())
            {
                imgURLList.add(imgURLMatcher.group(1));
            }
        }
        wrapper.setImgURLList(imgURLList);
    }

    /**
     * extract platform information from {@link ThreadContentItem}'s mainText and
     * set these inform to  {@link ThreadContentItem}'s platformInfo field.
     * @param itemData ThreadContentItem object
     */
    private static void extractPlatform(ThreadContentItem itemData)
    {
        Pattern platformPattern = Pattern.compile("(posted by wap, platform: .+?)\\s*<br(\\s*?\\/)*?>");
        Pattern androidPattern = Pattern.compile(APIURL.ANDROID_CLIENT_SIGNATURE_REGEX);
        Matcher platformMatcher = platformPattern.matcher(itemData.getMainText());
        if (platformMatcher.find())
        {
            itemData.setPlatformInfo(platformMatcher.group(1));
            itemData.setMainText(itemData.getMainText().replaceAll(platformPattern.pattern(), ""));
        }
        Matcher androidMatcher = androidPattern.matcher(itemData.getMainText());
        if (androidMatcher.find())
        {
            itemData.setPlatformInfo(androidMatcher.group(1));
            itemData.setMainText(itemData.getMainText().replaceAll(androidPattern.pattern(), ""));
        }
        itemData.setMainText(HtmlUtils.cleanText(itemData.getMainText()));
        itemData.setMainText(HtmlUtils.cleanEditHistory(itemData.getMainText()));
    }

    @Nullable
    public List<ThreadContentItem> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ThreadContentItem> replyList) {
        this.replyList = replyList;
    }

    @Nullable
    public int getTid() {
        return tid;
    }

    public void setTid(@Nullable int tid) {
        this.tid = tid;
    }

    @Nullable
    public int getFid() {
        return fid;
    }

    public void setFid(@Nullable int fid) {
        this.fid = fid;
    }

    @Nullable
    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(@Nullable int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    @Nullable
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(@Nullable int currentPage) {
        this.currentPage = currentPage;
    }

    @Nullable
    public int getTotalReplyCount() {
        return totalReplyCount;
    }

    public void setTotalReplyCount(@Nullable int totalReplyCount) {
        this.totalReplyCount = totalReplyCount;
    }

    @Nullable
    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(@Nullable boolean closed) {
        isClosed = closed;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public List<String> getImgURLList() {
        return imgURLList;
    }

    public void setImgURLList(@Nullable List<String> imgURLList) {
        this.imgURLList = imgURLList;
    }
}
