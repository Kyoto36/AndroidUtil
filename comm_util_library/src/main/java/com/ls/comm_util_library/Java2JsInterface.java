package com.ls.comm_util_library;

import android.content.Context;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class Java2JsInterface {

    private Gson mGson;

    public Java2JsInterface(Gson gson){
        mGson = gson;
    }

    @JavascriptInterface
    public void sayHelo(){
        LogUtils.INSTANCE.d(getClass().getSimpleName(),"android:sayHelo();");
    }

    public void jsSayHello(WebView webView){
        LogUtils.INSTANCE.d(getClass().getSimpleName(),"javascript:hello();");
        callJsFunction(webView,"javascript:hello();");
    }

    public void jsInsertImages(WebView webView,List<String> urls){
        String jsonList = mGson.toJson(urls,new TypeToken<List<String>>() {}.getType());
        callJsFunction(webView,"javascript:insertImgs(" + jsonList + ");");
    }

    private void callJsFunction(WebView webView,String trigger){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(trigger, null);
        } else {
            webView.loadUrl(trigger);
        }
    }
}
