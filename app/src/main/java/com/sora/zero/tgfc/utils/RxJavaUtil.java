package com.sora.zero.tgfc.utils;

import com.google.common.base.Supplier;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Forked from yrank
 */

public final class RxJavaUtil {
    public static final Object NULL = "";

    private RxJavaUtil() {
    }

    /**
     * @see Disposable#dispose()
     */
    public static void disposeIfNotNull(Disposable disposable) {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    /**
     * 快速发送工作线程和UI回调
     *
     * @param workAction 工作线程
     * @param uiAction   主线程
     * @return 订单
     */
    public static Disposable workWithUiThread(Action workAction, Action uiAction) {
        return workWithUiThread(workAction, uiAction, L::report);
    }

    /**
     * 快速发送工作线程和UI回调
     *
     * @param workAction 工作线程
     * @param uiAction   主线程
     * @param error      错误回调
     * @return 订单
     */
    public static Disposable workWithUiThread(Action workAction, Action uiAction, Consumer<Throwable> error) {
        return Observable.just(NULL)
                .doOnNext(i -> workAction.run())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> uiAction.run(), error);
    }

    /**
     * 快速发送工作线程和有返回值的UI回调
     *
     * @param workAction 工作线程
     * @param uiAction   主线程
     * @param <R>        返回值的类型
     * @return 订单
     */
    public static <R> Disposable workWithUiResult(Supplier<R> workAction, Consumer<R> uiAction) {
        return workWithUiResult(workAction, uiAction, L::report);
    }

    /**
     * 快速发送工作线程和有返回值的UI回调
     *
     * @param workAction 工作线程
     * @param uiAction   主线程
     * @param error      错误回调
     * @param <R>        返回值的类型
     * @return 订单
     */
    public static <R> Disposable workWithUiResult(Supplier<R> workAction, Consumer<R> uiAction, Consumer<Throwable> error) {
        return Observable.just(NULL)
                .map(n -> workAction.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uiAction, error);
    }

    public static <T> ObservableTransformer<T, T> iOTransformer() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> iOSingleTransformer() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> clickThrottleTransformer() {
        return observable -> observable.throttleFirst(1, TimeUnit.SECONDS);
    }
}
