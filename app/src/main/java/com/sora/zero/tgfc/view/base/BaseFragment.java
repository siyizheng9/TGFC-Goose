package com.sora.zero.tgfc.view.base;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.utils.L;
import com.sora.zero.tgfc.view.MainActivity;

/**
 * Created by zsy on 3/25/17.
 */

public abstract class BaseFragment extends Fragment {

    protected String fragmentTitle;

    protected String LOG_TAG;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        L.d(LOG_TAG + " onAttach");

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        L.d(LOG_TAG + " onCreate");

    }

    // onCreateView

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        L.d(LOG_TAG + "onActivityCreated");

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        L.d(LOG_TAG + "onViewStateRestored");
    }

    @Override
    public void onStart() {
        super.onStart();
        L.d(LOG_TAG + " onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        L.d(LOG_TAG + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        L.d(LOG_TAG + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        L.d(LOG_TAG + " onStop");
    }

    @Override
    public void onDestroyView(){
        L.d(LOG_TAG + " onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        L.d(LOG_TAG + " onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        L.d(LOG_TAG + " onDetach");
        super.onDetach();

    }

    protected void setActionBarTitle(String title) {
        L.d("setActionBarTitle: " + title);
        if (getActivity() != null) {
            getActivity().setTitle(title);

        }
    }


    public String getTitle() {
        return this.fragmentTitle;
    }


}
