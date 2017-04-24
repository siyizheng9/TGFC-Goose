package com.sora.zero.tgfc.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.google.common.base.Optional;
import com.sora.zero.tgfc.App;
import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.api.model.ForumThread;
import com.sora.zero.tgfc.data.api.model.ThreadContentItem;
import com.sora.zero.tgfc.data.api.model.User;
import com.sora.zero.tgfc.data.api.model.UserInfo;
import com.sora.zero.tgfc.data.event.QuoteEvent;
import com.sora.zero.tgfc.widget.BezelImageView;
import com.sora.zero.tgfc.widget.EventBus;
import com.sora.zero.tgfc.widget.span.GlideImageGetter;
import com.sora.zero.tgfc.widget.span.HtmlCompat;

import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zsy on 3/30/17.
 */

public class BindingAdapterUtils {
    @BindingAdapter({"forumThread"})
    public static void setForumThread(TextView view, ForumThread thread){
        view.setText(thread.getTitle());
        String repliesStr = StringUtils.TWO_SPACES + thread.getReplyCount();
        int start = view.length();
        view.append(repliesStr);
        ViewUtils.setForegroundColor(view,
                ContextCompat.getColor(App.get(), R.color.floor_num), start, view.length());
    }

    @BindingAdapter({"post", "eventBus"})
    public static void setFloorNum(TextView textView, ThreadContentItem post, EventBus eventBus){
        String floorNum = "#" + post.getFloorNum();
        textView.setText(floorNum);
        textView.setOnClickListener(v -> {
            eventBus.post(new QuoteEvent(post));
        });
    }

    @BindingAdapter({"relativeTime"})
    public static void setRelativeTime(TextView view, @Nullable String postTime){
        Optional<Date> date = StringUtils.dateFromString(postTime);
        if(date.isPresent() == false)
            view.setText(postTime);
        else {
            //L.d("postTime:" + postTime + " date: " + date);
            view.setText(DateUtils.getRelativeDateTimeString(view.getContext(), date.get().getTime(),
                    DateUtils.MINUTE_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0));
        }
    }

    @BindingAdapter({"reply"})
    public static void setReply(TextView textView, @NonNull ThreadContentItem post) {
        String mainText = post.getMainText();

        if (TextUtils.isEmpty(mainText))
            textView.setText(null);
        else {
            textView.setText(
                    HtmlCompat.fromHtml(mainText, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST | HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM,
                    GlideImageGetter.get(textView), null));
        }
    }

    @BindingAdapter("quotedText")
    public static void setQuotedText(TextView textView, @NonNull ThreadContentItem post) {
        if(!TextUtils.isEmpty(post.getQuotedText())){
            //L.d("Quoted info:\n" + post.getQuotedInfo() +"Quoted text:\n" + post.getQuotedText());
            String quotedText = post.getQuotedInfo() + "<br>" + post.getQuotedText();
            textView.setVisibility(View.VISIBLE);
            textView.setText(StringUtils.fromHtml(quotedText));
        } else {
            textView.setVisibility(View.GONE);
        }

    }
    @BindingAdapter("user")
    public static void setUser(BezelImageView bezelImageView, User user) {
        L.d("setUser user");
        if(user.isLogged()){

            Context context = bezelImageView.getContext();

            if (ActivityUtils.isActivityDestroyedForGlide(context)) {
                return;
            }

            Glide.with(context)
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.avatar_goose)
                    .error(R.drawable.avatar_goose)
                    .crossFade()
                    .centerCrop()
                    .into(bezelImageView);

        }

    }


    @BindingAdapter({"uid"})
    public static void loadAvatar(BezelImageView bazelImageView, int uid) {

        if(!App.getAppComponent().getUser().isLogged())
            return;

        App.getAppComponent().getTGFCService()
                .getUserInfo(String.valueOf(uid))
                .map(UserInfo::fromSource)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        optional -> {
                            if(optional.isPresent()){

                                Context context = bazelImageView.getContext();

                                if (ActivityUtils.isActivityDestroyedForGlide(context)) {
                                    return;
                                }

                                Glide.with(context)
                                        .load(optional.get().getAvatarUrl())
                                        .placeholder(R.drawable.avatar_goose)
                                        .error(R.drawable.avatar_goose)
                                        .crossFade()
                                        .centerCrop()
                                        .into(bazelImageView);

                            }
                        }
                );
    }

    @BindingAdapter({"emoticonDrawableRequestBuilder", "emoticonImagePath"})
    public static void loadEmoticon(ImageView imageView,
                                    DrawableRequestBuilder<Uri> emoticonDrawableRequestBuilder,
                                    String emoticonImagePath) {
        emoticonDrawableRequestBuilder.load(Uri.parse(emoticonImagePath)).into(imageView);
    }


}
