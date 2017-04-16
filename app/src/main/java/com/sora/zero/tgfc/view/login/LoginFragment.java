package com.sora.zero.tgfc.view.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.databinding.FragmentLoginBinding;
import com.sora.zero.tgfc.utils.L;
import com.sora.zero.tgfc.view.MainActivity;
import com.sora.zero.tgfc.view.base.BaseFragment;

/**
 * Created by zsy on 4/10/17.
 */

public class LoginFragment extends BaseFragment implements LoginContract.View {

    private FragmentLoginBinding mBinding;

    private LoginContract.Presenter mPresenter;

    private ProgressDialog mProgressDialog;

    @Override
    public void onAttach(Context context) {
        this.LOG_TAG = "LoginFragment";
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        L.d(LOG_TAG + " onCreateView");

        this.fragmentTitle = getString(R.string.action_login);

        mPresenter = new LoginPresenter();
        mPresenter.setView(this);

        mProgressDialog = new ProgressDialog(getActivity(),
                R.style.Theme_AppCompat_Dialog);

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, parent, false);

        mBinding.btnLogin.setOnClickListener(v -> {
            if(validate() == false)
                return;
            mPresenter.login(mBinding.inputUsername.getText().toString(),
                    mBinding.inputPassword.getText().toString());
        });

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(fragmentTitle);

    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        ((MainActivity) getActivity()).setDrawerToggleIndicator(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        L.d(LOG_TAG + " onOptionsItemSelected itemId: " + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setLoadingIndicator(boolean active){

        if(active) {
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Authenticating...");
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }

    }

    @Override
    public void showMessage(String message){

        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public boolean isActive(){
        return isAdded();
    }


    @Override
    public void setPresenter(@NonNull LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void finish(){
        getActivity().onBackPressed();
    }

    private boolean validate(){
        boolean valid = true;

        String username = mBinding.inputUsername.getText().toString();
        String password = mBinding.inputPassword.getText().toString();

        if (username.isEmpty()) {
            mBinding.inputUsername.setError("enter a valid username");
            valid = false;
        } else {
            mBinding.inputUsername.setError(null);
        }

        if (password.isEmpty()) {
            mBinding.inputPassword.setError("enter a valid password");
            valid = false;
        } else {
            mBinding.inputPassword.setError(null);
        }

        return valid;
    }

}
