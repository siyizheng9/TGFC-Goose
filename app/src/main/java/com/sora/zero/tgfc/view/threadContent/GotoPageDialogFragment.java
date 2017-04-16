package com.sora.zero.tgfc.view.threadContent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.utils.L;
import com.sora.zero.tgfc.view.login.LoginContract;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by zsy on 4/16/17.
 */

public class GotoPageDialogFragment extends DialogFragment {

    private final String LOG_TAG = "GotoPageDialogFragment";

    private static final String ARG_TOTALPAGE = "total_page";
    private static final String ARG_CURRENTPAGE = "current_page";

    private int mTotalPage;
    private int mCurrentPage;
    private NumberPicker mNumberPicker;
    private PublishSubject<Integer> onPageSelectSubject = PublishSubject.create();

    public GotoPageDialogFragment() {

    }

    public static GotoPageDialogFragment newInstance(int totalPage, int currentPage) {
        GotoPageDialogFragment fragment = new GotoPageDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TOTALPAGE, totalPage);
        args.putInt(ARG_CURRENTPAGE, currentPage);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        L.d(LOG_TAG + " onCreateDialog");

        if(savedInstanceState != null) {
            this.mTotalPage = savedInstanceState.getInt(ARG_TOTALPAGE);
            this.mCurrentPage = savedInstanceState.getInt(ARG_CURRENTPAGE);
        } else {
            Bundle args = getArguments();
            this.mCurrentPage = args.getInt(ARG_CURRENTPAGE);
            this.mTotalPage = args.getInt(ARG_TOTALPAGE);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.action_goto_page);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_goto_page, null);
        mNumberPicker = (NumberPicker) dialogView.findViewById(R.id.number_picker);
        mNumberPicker.setMaxValue(mTotalPage);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setValue(mCurrentPage);

        builder.setView(dialogView);
        builder.setPositiveButton(R.string.action_ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onPageSelectSubject.onNext(mNumberPicker.getValue());
                    }
                });
        builder.setNegativeButton(R.string.action_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }

                    }
                });

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_TOTALPAGE, this.mTotalPage);
        outState.putSerializable(ARG_CURRENTPAGE, this.mCurrentPage);

        super.onSaveInstanceState(outState);
    }

    public Observable<Integer> getPageSelect() {
        return onPageSelectSubject;
    }
}
