package com.ls.rxjava_library;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.ls.comm_util_library.ObjectUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @ClassName: RxClick
 * @Description:
 * @Author: ls
 * @Date: 2020/9/15 17:39
 */
public class RxView {
    /**
     * 点击防抖
     *
     * @param view
     * @param num
     * @param unit
     * @return
     */
    public static Observable<View> click(final View view, long num, TimeUnit unit) {
        ObjectUtil.requireNonNull(view, "view not is null");
        return Observable.create((ObservableOnSubscribe<View>) emitter -> {
            view.setOnClickListener(v -> {
                if (!emitter.isDisposed()) {
                    emitter.onNext(v);
                }
            });
        }).throttleFirst(num, unit).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<View> click(final View view, long millis) {
        return click(view, millis, TimeUnit.MILLISECONDS);
    }

    /**
     * textView的内容改变监听
     * 每个内容在延迟指定时间后发出，如果中途改变内容，就会取消上一次内容发射，重新计算时间
     * @param textView
     * @param num
     * @param unit
     * @return
     */
    public static Observable<String> textChange(final TextView textView, long num, TimeUnit unit) {
        ObjectUtil.requireNonNull(textView, "textView not is null");
        return Observable.create(
                (ObservableOnSubscribe<String>) emitter -> {
                    textView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) { }

                        @Override
                        public void afterTextChanged(Editable s) {
                            emitter.onNext(s.toString());
                        }
                    });
                })
                .debounce(num, unit)
                .observeOn(AndroidSchedulers.mainThread());
    }

}
