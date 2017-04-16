package com.sora.zero.tgfc.view.threadList;

import com.sora.zero.tgfc.AppComponent;
import com.sora.zero.tgfc.utils.FragmentScoped;
import com.sora.zero.tgfc.view.MainActivity;

import dagger.Component;

/**
 * Created by zsy on 3/30/17.
 */

@FragmentScoped
@Component(dependencies = AppComponent.class, modules = ThreadListPresenterModule.class)
public interface ThreadListComponent {

    void inject(MainActivity activity);
}
