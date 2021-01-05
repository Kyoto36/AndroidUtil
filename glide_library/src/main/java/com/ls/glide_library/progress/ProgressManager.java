package com.ls.glide_library.progress;

import android.os.Handler;
import android.os.HandlerThread;

import com.bumptech.glide.load.engine.GlideException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @ClassName: ProgressManager
 * @Description:
 * @Author: ls
 * @Date: 2020/12/29 19:19
 */
public class ProgressManager {
    private static final long LOAD_IMAGE_TIMEOUT = 200;
    private static ProgressManager instance;

    public static ProgressManager get(){
        if(instance == null){
            synchronized (ProgressManager.class){
                if(instance == null){
                    instance = new ProgressManager();
                }
            }
        }
        return instance;
    }

    private HandlerThread mWorkThread;
    private Handler mWorkHandler;
    private OkHttpClient mOkHttpClient;
    private Map<String,FirstProgressListener> mListenerMap = new HashMap<>();
    private OnProgressListener mGlobalListener = new OnProgressListener() {
        @Override
        public void onProgress(final String url, final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
            mWorkHandler.post(new Runnable() {
                @Override
                public void run() {
                    FirstProgressListener listener = mListenerMap.get(url);
                    if(listener != null) {
                        listener.onProgress(url, bytesRead, totalBytes, isDone, exception);
                    }
                }
            });

        }
    };

    private ProgressManager(){
        mWorkThread = new HandlerThread("glide-progress-thread");
        mWorkThread.start();
        mWorkHandler = new Handler(mWorkThread.getLooper());
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BASIC);
        mOkHttpClient = new OkHttpClient.Builder()
//                .readTimeout(LOAD_IMAGE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        return response.newBuilder()
                                .body(new ProgressResponseBody(request.url().toString(), response.body(), mGlobalListener))
                                .build();
                    }
                }).build();
    }

    public OkHttpClient getOkHttp(){
        return mOkHttpClient;
    }

    public void registerListener(final String url, final OnProgressListener listener){
        mWorkHandler.post(new Runnable() {
            @Override
            public void run() {
                FirstProgressListener firstListener = mListenerMap.get(url);
                if(firstListener == null){
                    firstListener = new FirstProgressListener();
                    mListenerMap.put(url,firstListener);
                }
                firstListener.register(listener);
            }
        });
    }

    public void unRegisterListener(final String url, final OnProgressListener listener){
        mWorkHandler.post(new Runnable() {
            @Override
            public void run() {
                FirstProgressListener firstListener = mListenerMap.get(url);
                if(firstListener == null){
                    return;
                }
                boolean remove = firstListener.unRegister(listener);
                if(remove){
                    mListenerMap.remove(url);
                }
            }
        });
    }

    static class FirstProgressListener implements OnProgressListener{

        private Set<WeakReference<OnProgressListener>> listeners = new CopyOnWriteArraySet<>();

        public void register(OnProgressListener listener){
            if(!isExits(listener)) {
                listeners.add(new WeakReference<>(listener));
                clear();
            }
        }

        /**
         * 返回内部维护列表是否为空，如果为true，标识可以删除改url的监听
         * @param listener
         * @return
         */
        public boolean unRegister(OnProgressListener listener){
            List<WeakReference<OnProgressListener>> removeReferences = new ArrayList<>();
            for (WeakReference<OnProgressListener> reference : listeners){
                if(reference.get() == listener || reference.get() == null){
                    removeReferences.add(reference);
                }
            }
            for (WeakReference<OnProgressListener> reference : removeReferences){
                listeners.remove(reference);
            }
            return listeners.isEmpty();
        }

        /**
         *
         * @param listener
         * @return
         */
        private boolean isExits(OnProgressListener listener){
            for (WeakReference<OnProgressListener> reference : listeners){
                if(reference != null && reference.get() == listener){
                    return true;
                }
            }
            return false;
        }

        /**
         * 判断内部维护列表是否为空，如果为true，标识可以删除改url的监听
         * @return
         */
        public boolean clear(){
            List<WeakReference<OnProgressListener>> removeReferences = new ArrayList<>();
            for (WeakReference<OnProgressListener> reference : listeners){
                if(reference.get() == null){
                    removeReferences.add(reference);
                }
            }
            for (WeakReference<OnProgressListener> reference : removeReferences){
                listeners.remove(reference);
            }
            return listeners.isEmpty();
        }

        @Override
        public void onProgress(String url, long bytesRead, long totalBytes, boolean isDone, GlideException exception) {
            for (WeakReference<OnProgressListener> reference : listeners){
                if(reference != null && reference.get() != null){
                    reference.get().onProgress(url, bytesRead, totalBytes, isDone, exception);
                }
            }
        }
    }

}
