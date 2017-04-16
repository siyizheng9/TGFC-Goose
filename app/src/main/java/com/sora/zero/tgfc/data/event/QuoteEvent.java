package com.sora.zero.tgfc.data.event;

import com.sora.zero.tgfc.data.api.model.ThreadContentItem;

/**
 * Created by zsy on 4/15/17.
 */

public class QuoteEvent {

    private final ThreadContentItem quotePost;

    public QuoteEvent(ThreadContentItem quotePost) {
        this.quotePost =quotePost;
    }

    public ThreadContentItem getQuotePost() {
        return quotePost;
    }
}
