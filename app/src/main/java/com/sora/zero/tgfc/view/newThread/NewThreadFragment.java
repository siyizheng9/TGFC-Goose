package com.sora.zero.tgfc.view.newThread;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.sora.zero.tgfc.App;
import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.api.APIURL;
import com.sora.zero.tgfc.data.api.TGFCService;
import com.sora.zero.tgfc.data.api.model.ThreadType;
import com.sora.zero.tgfc.data.api.model.wrapper.ThreadTypeWrapper;
import com.sora.zero.tgfc.databinding.FragmentNewThreadBinding;
import com.sora.zero.tgfc.utils.L;
import com.sora.zero.tgfc.utils.RxJavaUtil;
import com.sora.zero.tgfc.view.base.BasePostFragment;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Forked from yrank
 * A Fragment shows {@link EditText} to let users input reply.
 */

public class NewThreadFragment extends BasePostFragment {

    private static final String ARG_FORUM_ID = "forum_id";

    private TGFCService mTGFCService;

    private int mForumId;
    private Disposable mDisposable;

    private EditText titleEditText;
    private Spinner typeSpinner;

    private NewThreadCacheModel mCacheModel;
    private List<ThreadType> mThreadTypes;

    public static NewThreadFragment newInstance(int forumId) {
        NewThreadFragment fragment = new NewThreadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_FORUM_ID, forumId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        this.LOG_TAG = "NewThreadFragment";
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.fragmentTitle = getString(R.string.action_new_thread);

        FragmentNewThreadBinding newThreadBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_new_thread, container, false);
        initCreateView(newThreadBinding.layoutPost);
        titleEditText = newThreadBinding.title;
        typeSpinner = newThreadBinding.spinner;
        return newThreadBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mForumId = getArguments().getInt(ARG_FORUM_ID);

        mTGFCService = App.getAppComponent().getTGFCService();

        init(); //set spinner options

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(fragmentTitle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onDestroy() {

        if (mDisposable != null)
            mDisposable.dispose();

        super.onDestroy();
    }

    @Override
    protected void restoreFromCache(Bundle savedInstanceState) {
        mCacheModel =  savedInstanceState.getParcelable(ARG_CACHE_MODEL);
        mThreadTypes = mCacheModel.getThreadTypes();
        setSpinner(mThreadTypes);
        if(mCacheModel != null){
            titleEditText.setText(mCacheModel.getTitle());
            mReplyView.setText(mCacheModel.getMessage());
        }

    }

    @Override
    protected void cacheContent(Bundle outState){
        outState.putParcelable(ARG_CACHE_MODEL, new NewThreadCacheModel(
                typeSpinner.getSelectedItemPosition(),
                titleEditText.getText().toString(),
                getContent(),
                mThreadTypes));

    }

    private void init() {
        mDisposable = mTGFCService
                .getThreadTypes(APIURL.WEB_FORUM + mForumId)
                .map(ThreadTypeWrapper::fromSource)
                .compose(RxJavaUtil.iOTransformer())
                .subscribe(threadTypeWrapper -> {
                    if (threadTypeWrapper != null && threadTypeWrapper.hasError == false) {
                        setSpinner(threadTypeWrapper.getThreadTypes());
                    } else {
                        showMessage(getString(R.string.message_network_error));
                        typeSpinner.setVisibility(View.GONE);
                    }
                }, e -> {
                    L.e(LOG_TAG, e.toString());
                });
    }

    private void setSpinner(@NonNull List<ThreadType> types) {
        if (types.isEmpty()){
            typeSpinner.setVisibility(View.GONE);
            return;
        } else {
            typeSpinner.setVisibility(View.VISIBLE);
        }
        mThreadTypes = types;
        ThreadTypeSpinnerAdapter adapter = new ThreadTypeSpinnerAdapter(this.getContext(), types);
        typeSpinner.setAdapter(adapter);
        if (mCacheModel != null && types.size() > mCacheModel.getSelectPosition()) {
            typeSpinner.setSelection(mCacheModel.getSelectPosition());
        }

    }

    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }


}
