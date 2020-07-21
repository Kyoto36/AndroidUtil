package com.ls.comm_util_library;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.content.ClipboardManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtUtils {
    //length用户要求产生字符串的长度
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String http2Https(String url){
        if(url.startsWith("http://")){
            url = url.replace("http://","https://");
        }
        return url;
    }

    public static String repeat(String str,int count){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++){
            sb.append(str);
        }
        return sb.toString();
    }

    public static String repeat(String str,String separat,int count){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++){
            sb.append(str).append(separat);
        }
        return sb.delete(sb.length() - 1,sb.length()).toString();
    }

    public static String numberFormat(long number){
        if(number <= 0){
            return "";
        }
        else if(number >= 10000){
            DecimalFormat decimalFormat = new DecimalFormat("0.##");
            decimalFormat.setRoundingMode(RoundingMode.FLOOR);
            return String.format("%s万",decimalFormat.format((float)number / 10000));
        }
        else{
            return number + "";
        }
    }

    public static void copyToCilp(Context context,String label,String content){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型
        // 将ClipData内容放到系统剪贴板里。
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, content));
        Toast.makeText(context,"已复制到剪切板",Toast.LENGTH_LONG).show();
    }

    public static void interceptUrlClick(TextView view,ISingleListener<String> listener){
        view.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = view.getText();
        if(text instanceof Spannable){
            Spannable sp = (Spannable) text;
            URLSpan[] urls = sp.getSpans(0,text.length(),URLSpan.class);
            if(urls.length == 0){
                return;
            }
            SpannableStringBuilder spannable = new SpannableStringBuilder(text);
            for (URLSpan url: urls){
                spannable.setSpan(new CustomClickSpan(url.getURL(), listener),sp.getSpanStart(url),sp.getSpanEnd(url),Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            view.setText(spannable);
        }
    }

    public static void setStringClickable(SpannableString string, int color, View.OnClickListener listener){
        setStringClickable(string, color,false, listener);
    }

    public static void setStringClickable(SpannableString string, int color,boolean underline, View.OnClickListener listener){
        string.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                listener.onClick(widget);
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //设置颜色
                ds.setColor(color);
                //设置是否要下划线
                ds.setUnderlineText(underline);
            }

        },0,string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static void setLinkClickable(final Context context, final SpannableStringBuilder clickableHtmlBuilder,
                                         final URLSpan urlSpan,int color) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
        ClickableSpan clickableSpan = new ClickableSpan() {

            public void onClick(View view) {
                //Do something with URL here.
                String url = urlSpan.getURL();
            }

            public void updateDrawState(TextPaint ds) {
                //设置颜色
                ds.setColor(color);
                //设置是否要下划线
                ds.setUnderlineText(true);
            }

        };
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }

    public static CharSequence getClickableLink(Context context, String html, int color) {
        Spanned spannedHtml = Html.fromHtml(html);
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            setLinkClickable(context,clickableHtmlBuilder, span,color);
        }
        return clickableHtmlBuilder;
    }

    public static int getTextSize(String text){
        if(android.text.TextUtils.isEmpty(text)){
            return 0;
        }
        return text.length();
    }

    public static String formatPhone(String phone){
        StringBuilder sb = new StringBuilder(phone);
        if (phone.length() == 4) {
            if (phone.endsWith(" ")) {
                return sb.deleteCharAt(sb.length() - 1).toString();
            }
            return sb.insert(3, " ").toString();
        }
        if (phone.length() == 9) {
            if (phone.endsWith(" ")) {
                return sb.deleteCharAt(sb.length() - 1).toString();
            }
            return sb.insert(8, " ").toString();
        }
        return "";
    }

    public static String formatQQ(String QQ){
        StringBuilder sb = new StringBuilder(QQ);
        if (QQ.length() == 4) {
            if (QQ.endsWith(" ")) {
                return sb.deleteCharAt(sb.length() - 1).toString();
            }
            return sb.insert(3, " ").toString();
        }
        if (QQ.length() == 8) {
            if (QQ.endsWith(" ")) {
                return sb.deleteCharAt(sb.length() - 1).toString();
            }
            return sb.insert(7, " ").toString();
        }
        if((QQ.length() - 8) > 0 && (QQ.length() - 8) % 5 == 0){
            if (QQ.endsWith(" ")) {
                return sb.deleteCharAt(sb.length() - 1).toString();
            }
            return sb.insert(QQ.length() - 1, " ").toString();
        }
        return "";
    }

    private static final String REGEX_MOBILE = "^1([3-9])\\d{9}$";
    public static boolean isMobilePhone(String phone){
        if(android.text.TextUtils.isEmpty(phone) || phone.length() < 11){
            return false;
        }
        if(phone.matches(REGEX_MOBILE)){
            return true;
        }
        return false;
    }

    private static final String REGEX_IDCARD = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[X|x|0-9]$";
    public static boolean isIdCard(String idCard){
        if(TextUtils.isEmpty(idCard) || (idCard.length() != 15 && idCard.length() != 18)){
            return false;
        }
        if(idCard.matches(REGEX_IDCARD)){
            return true;
        }
        return false;
    }
}
