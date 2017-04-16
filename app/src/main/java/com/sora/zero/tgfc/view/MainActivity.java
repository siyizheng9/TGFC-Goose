package com.sora.zero.tgfc.view;

import android.app.Dialog;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sora.zero.tgfc.App;
import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.api.model.Forum;
import com.sora.zero.tgfc.data.api.model.ForumList;
import com.sora.zero.tgfc.data.api.model.User;
import com.sora.zero.tgfc.databinding.ActivityMainBinding;
import com.sora.zero.tgfc.databinding.NavHeaderBinding;
import com.sora.zero.tgfc.utils.FragmentUtils;
import com.sora.zero.tgfc.utils.L;
import com.sora.zero.tgfc.utils.PreferenceUtils;
import com.sora.zero.tgfc.view.base.BaseActivity;
import com.sora.zero.tgfc.view.base.BaseFragment;
import com.sora.zero.tgfc.view.login.LoginFragment;
import com.sora.zero.tgfc.view.threadList.DaggerThreadListComponent;
import com.sora.zero.tgfc.view.threadList.ThreadListFragment;
import com.sora.zero.tgfc.view.threadList.ThreadListPresenter;
import com.sora.zero.tgfc.view.threadList.ThreadListPresenterModule;

import java.util.List;

import javax.inject.Inject;


public class MainActivity extends BaseActivity {

    private final String LOG_TAG = "MainActivity";
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;

    private ActivityMainBinding binding;

    @Inject ThreadListPresenter mThreadListPresenter;

    @Inject User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(LOG_TAG + " onCreate");


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Set a Toolbar to replace the ActionBar.
        mToolbar = binding.toolbarbinding.toolbar;
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Find our drawer view
        mDrawer = binding.drawerLayout;
        mNavigationView = binding.nvView;

        Menu NavMenu = mNavigationView.getMenu();
        setupPinnedForumList(NavMenu);


        View headerLayout = mNavigationView.getHeaderView(0);
        mDrawerToggle = setupDrawerToggle();

        setupDrawerContent(mNavigationView);
        mDrawer.addDrawerListener(mDrawerToggle);


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flContent);
        ThreadListFragment threadListFragment;

        if(fragment == null) {

            threadListFragment = ThreadListFragment.newInstance(
                    ForumList.getForumByFid(PreferenceUtils.getLastViewedForumId()).name);

            FragmentUtils.addFragmentToActivity(
                    getSupportFragmentManager(), threadListFragment, R.id.flContent);

        } else {
            threadListFragment =
                    (ThreadListFragment) getSupportFragmentManager().
                            findFragmentByTag(ThreadListFragment.class.getName());
        }

        DaggerThreadListComponent.builder()
                .appComponent(App.getAppComponent())
                .threadListPresenterModule(new ThreadListPresenterModule(threadListFragment))
                .build().inject(this);

        setupNavDrawerHeader(mNavigationView);

        getSupportFragmentManager().addOnBackStackChangedListener(
                () -> {
                    BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
                    L.d("onBackStackChanged " + baseFragment.getClass().getName());
                    if(baseFragment instanceof ThreadListFragment)
                        setDrawerToggleIndicator(true);

                    if(baseFragment != null)
                        setTitle(baseFragment.getTitle());
                }
        );


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

       mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_frame, menu);
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        L.d(LOG_TAG + " onOptionsItemSelected" + " itemId: " + item.getItemId());
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

       return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
            return;
        }

        super.onBackPressed();
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                //fragmentClass = ThreadListFragment.class;
                break;
            default:
               // L.d(String.valueOf(menuItem.getItemId()));
                //forum id = item id
                FragmentUtils.showForum(getSupportFragmentManager(), mThreadListPresenter, menuItem.getItemId());
                PreferenceUtils.setLastViewedForumId(menuItem.getItemId());
                mDrawer.closeDrawers();
                return;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(fragment == null)
            return;

        // Inset the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }


    private ActionBarDrawerToggle setupDrawerToggle() {

        return new ActionBarDrawerToggle(this, mDrawer, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener( menuItem -> {selectDrawerItem(menuItem); return true;});
    }

    private void setupNavDrawerHeader(NavigationView navigationView) {
        NavHeaderBinding binding =  DataBindingUtil.bind(navigationView.getHeaderView(0));

        if(!mUser.isLogged())
            mUser.init();

        binding.setUser(mUser);

        binding.drawerUserAvatar.setOnClickListener(v ->
            binding.drawerUserName.performClick() );

        binding.drawerUserName.setOnClickListener(v -> {
            if(!mUser.isLogged()){
                LoginFragment fragment = new LoginFragment();
                FragmentUtils.showFragment(getSupportFragmentManager(), fragment);
                mDrawer.closeDrawers();
            } else {
                Dialog dialog = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.action_logout))
                        .setPositiveButton(getString(R.string.ok),
                                (d, which) -> {
                                    App.getAppComponent().getCookieManager()
                                            .getCookieStore().removeAll();
                                    mUser.setLogged(false);
                                    showMessage("成功退出登陆");

                                } )
                        .setNegativeButton(getString(R.string.cancel),
                                (d, which) -> {}).create();
                dialog.show();
            }
        });
    }

    private void setupPinnedForumList(Menu NavMenu) {

        MenuItem subitems =  NavMenu.findItem(R.id.sub_menu);
        Menu subM = subitems.getSubMenu();


        List<Forum> pinnedForumList = ForumList.getPinnedForumList();
        for(Forum f : pinnedForumList) {
            subM.add(0, f.fid, Menu.NONE, f.name).setIcon(R.drawable.ic_one);
        }

    }

    public void setDrawerToggleIndicator(boolean active){
        mDrawerToggle.setDrawerIndicatorEnabled(active);
    }

    public void showMessage(String message) {
        Snackbar.make(binding.flContent, message, Snackbar.LENGTH_LONG).show();
    }



}
