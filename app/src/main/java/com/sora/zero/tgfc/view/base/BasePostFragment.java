package com.sora.zero.tgfc.view.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.sora.zero.tgfc.App;
import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.event.EmoticonClickEvent;
import com.sora.zero.tgfc.databinding.FragmentPostBinding;
import com.sora.zero.tgfc.utils.ImeUtils;
import com.sora.zero.tgfc.utils.RxJavaUtil;
import com.sora.zero.tgfc.view.MainActivity;
import com.sora.zero.tgfc.widget.Emoticon.EmoticonPagerAdapter;
import com.sora.zero.tgfc.widget.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * Forked from yrank
 */

public abstract class BasePostFragment extends BaseFragment {
    /**
     * The serialization (saved instance state) Bundle key representing whether emoticon
     * keyboard is showing when configuration changes.
     */
    private static final String STATE_IS_EMOTICON_KEYBOARD_SHOWING = "is_emoticon_keyboard_showing";;
    protected static final String ARG_CACHE_MODEL = "args_cache_model";
    private final Interpolator mInterpolator = new FastOutSlowInInterpolator();
    protected FragmentPostBinding mFragmentPostBinding;
    protected EditText mReplyView;
    /**
     * {@code mMenuEmoticon} is null before {@link #onCreateOptionsMenu(Menu, MenuInflater)}.
     */
    @Nullable
    protected MenuItem mMenuEmoticon;
    protected View mEmoticonKeyboard;
    /**
     * {@code mMenuSend} is null when configuration changes.
     */
    @Nullable
    protected MenuItem mMenuSend;
    EventBus mEventBus;
    private boolean mIsEmoticonKeyboardShowing;
    private Disposable mEmoticonClickDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentPostBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post, container,
                false);
        mReplyView = mFragmentPostBinding.reply;
        mEmoticonKeyboard = mFragmentPostBinding.emoticonKeyboard;
        return mFragmentPostBinding.getRoot();

    }

    protected void initCreateView(@NonNull FragmentPostBinding fragmentPostBinding){
        mFragmentPostBinding = fragmentPostBinding;
        mReplyView = mFragmentPostBinding.reply;
        mEmoticonKeyboard = mFragmentPostBinding.emoticonKeyboard;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mIsEmoticonKeyboardShowing = savedInstanceState.getBoolean(
                    STATE_IS_EMOTICON_KEYBOARD_SHOWING);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null)
            restoreFromCache(savedInstanceState);

        mEventBus = App.getAppComponent().getEventBus();

        mReplyView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mMenuSend != null) {
                    // disable send menu if the content of reply is empty
                    mMenuSend.setEnabled(!TextUtils.isEmpty(editable.toString()));
                }

            }
        });

        setupEmoticonKeyboard();

        if(savedInstanceState != null) {
            if(mIsEmoticonKeyboardShowing) {
                showEmoticonKeyboard();
            }
        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        mEmoticonClickDisposable = mEventBus.get()
                .ofType(EmoticonClickEvent.class)
                .subscribe(event -> {
                    mReplyView.getText().replace(mReplyView.getSelectionStart(),
                            mReplyView.getSelectionEnd(), event.getEmoticonEntity());
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        RxJavaUtil.disposeIfNotNull(mEmoticonClickDisposable);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        cacheContent(outState);

        outState.putBoolean(STATE_IS_EMOTICON_KEYBOARD_SHOWING, mIsEmoticonKeyboardShowing);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_reply, menu);

        mMenuEmoticon = menu.findItem(R.id.action_emoticon);
        if(mIsEmoticonKeyboardShowing){
            setKeyboardIcon();
        }

        mMenuSend = menu.findItem(R.id.action_send)
                .setEnabled(!TextUtils.isEmpty(mReplyView.getText()));

        ((MainActivity) getActivity()).setDrawerToggleIndicator(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_emoticon:
                if (mIsEmoticonKeyboardShowing) {
                    hideEmoticonKeyboard(true);
                } else {
                    showEmoticonKeyboard();
                }

                return true;
            case R.id.action_send:
                //todo onMenuSendClick();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupEmoticonKeyboard() {
        ViewPager viewPager = mFragmentPostBinding.emoticonKeyboardPager;
        viewPager.setAdapter(new EmoticonPagerAdapter(getActivity()));

        TabLayout tabLayout = mFragmentPostBinding.emoticonKeyboardTabLayout;
        tabLayout.setupWithViewPager(viewPager);
    }

    private void showEmoticonKeyboard() {
        mIsEmoticonKeyboardShowing = true;

        //hide keyboard
        ImeUtils.setShowSoftInputOnFocus(mReplyView, false);
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mReplyView.getWindowToken(), 0);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mEmoticonKeyboard.setVisibility(View.VISIBLE);

        ViewCompat.animate(mEmoticonKeyboard)
                .alpha(1)
                .translationY(0)
                .setInterpolator(mInterpolator)
                .withLayer()
                .setListener(new EmoticonKeyboardAnimator());

        setKeyboardIcon();

    }

    public void hideEmoticonKeyboard() {
        hideEmoticonKeyboard(false);
    }

    private void hideEmoticonKeyboard(boolean shouldShowKeyboard) {
        ViewCompat.animate(mEmoticonKeyboard)
                .alpha(0)
                .translationY(mEmoticonKeyboard.getHeight())
                .setInterpolator(mInterpolator)
                .withLayer()
                .setListener(new EmoticonKeyboardAnimator() {

                    @Override
                    public void onAnimationEnd(View view) {
                        mEmoticonKeyboard.setVisibility(View.GONE);

                        ImeUtils.setShowSoftInputOnFocus(mReplyView, true);
                        getActivity().getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                        if (shouldShowKeyboard) {
                            InputMethodManager inputMethodManager = (InputMethodManager)
                                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(mReplyView,
                                    InputMethodManager.SHOW_IMPLICIT);
                        }

                        super.onAnimationEnd(view);
                    }
                });

        mIsEmoticonKeyboardShowing = false;
        setEmoticonIcon();
    }

    private void setEmoticonIcon() {
        if (mMenuEmoticon != null) {
            mMenuEmoticon.setIcon(R.drawable.ic_insert_emoticon_black_24dp);
            mMenuEmoticon.setTitle(R.string.action_emoticon_keyboard);
        }
    }

    private void setKeyboardIcon() {
        if (mMenuEmoticon != null) {
            mMenuEmoticon.setIcon(R.drawable.ic_keyboard_black_24dp);
            mMenuEmoticon.setTitle(R.string.action_keyboard);
        }
    }

    @CallSuper
    public boolean isContentEmpty() {
        return mReplyView == null || TextUtils.isEmpty(mReplyView.getText());
    }

    @Nullable
    public String getContent() {
        if (mReplyView != null) {
            return mReplyView.getText().toString();
        }
        return null;
    }

    private class EmoticonKeyboardAnimator implements ViewPropertyAnimatorListener {

        @Override
        public void onAnimationStart(View view) {
            if (mMenuEmoticon != null) {
                mMenuEmoticon.setEnabled(false);
            }
        }

        @Override
        public void onAnimationEnd(View view) {
            if (mMenuEmoticon != null) {
                mMenuEmoticon.setEnabled(true);
            }
        }

        @Override
        public void onAnimationCancel(View view) {
        }
    }

    protected abstract void restoreFromCache(Bundle savedInstance);

    protected abstract void cacheContent(Bundle outState);



}
