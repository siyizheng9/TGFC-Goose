package com.sora.zero.tgfc.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by zsy on 3/22/17.
 */

public class HtmlUtils {

    public static String cleanQuote(String html)
    {
        String s = html.replaceAll("<\\/?i>", "").replaceAll("\\s{2}", " ");
        return s;
    }


    public static String cleanText(String html)
    {
        html = html.replaceAll("\\[color=#(.{6})\\]", "").replaceAll("\\[\\/color\\]", "");
        html = html.replaceAll("\\[size=([^\\[])+\\]", "").replaceAll("\\[\\/size\\]", "");
        html = html.replaceAll("(\\s*<\\/?br\\s*\\/?>\\s*){2,}", "<br />");
        Document doc = Jsoup.parse(html);
        return doc.body().html();
    }

    public static String getPlainText(String html)
    {
        Document htmlDoc = Jsoup.parse(html);
        htmlDoc.select("br").append("#br#");
        String text = htmlDoc.text();
        String s = text.replaceAll("(\\s*#br#\\s*)+", "\n").replaceAll("^\n", "").replaceAll("\n$", "");
        s = cleanEditHistory(s);
        s = s.replaceAll("(posted by wap, platform: .+?)\\s*", "").replaceAll("\\s*_{8,}\\s*(发送自.+?客户端)", "");
        return s;
    }

    public static String cleanEditHistory(String html)
    {
        return html
                .replaceAll("\\[?\\s*?(<i>)?\\s*本帖最后由.+?于.+?编辑\\s*(<\\/i>)?\\s*?\\]?", "");
    }

}
