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


    /**
     * 随机生成字符串
     *
     * @param length 用户要求产生字符串的长度
     * @return
     */
    public static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * http链接转https（慎用，服务端不支持https就尴尬了）
     *
     * @param url 需要转换的http链接
     * @return
     */
    public static String http2Https(String url) {
        if (url.startsWith("http://")) {
            url = url.replace("http://", "https://");
        }
        return url;
    }

    /**
     * 重复字符串
     *
     * @param str   需要重复的字符串
     * @param count 重复的次数
     * @return
     */
    public static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 重复字符串
     *
     * @param str   需要重复的字符串
     * @param limit 重复到指定位数
     * @return
     */
    public static String repeatTo(String str, int limit) {
        StringBuilder sb = new StringBuilder();
        while(sb.length() < limit){
            sb.append(str);
        }
        return sb.substring(0,limit);
    }

    /**
     * 重复字符串
     *
     * @param str     需要重复的字符串
     * @param separat 重复的分隔符
     * @param count   重复的次数
     * @return
     */
    public static String repeat(String str, String separat, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str).append(separat);
        }
        return sb.delete(sb.length() - 1, sb.length()).toString();
    }

    /**
     * 格式化数字，一般用于点赞数
     *
     * @param number 需要格式化的数字
     * @return
     * @deprecated Use {@link NumberUtils#numberFormat(long)} instead.
     */
    @Deprecated
    public static String numberFormat(long number) {
        return NumberUtils.numberFormat(number);
    }

    /**
     * 复制到剪切板
     *
     * @param context
     * @param label   复制的标签（标题）
     * @param content 复制的内容
     */
    public static void copyToCilp(Context context, String label, String content) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型
        // 将ClipData内容放到系统剪贴板里。
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, content));
//        Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_LONG).show();
    }

    /**
     * 拦截TextView中的链接点击事件，用于自定义跳转
     *
     * @param view     TextView
     * @param listener 点击监听
     * @deprecated use {@link ViewUtils#interceptUrlClick(TextView, ISingleListener<CharSequence>)}
     */
    @Deprecated
    public static void interceptUrlClick(TextView view, ISingleListener<CharSequence> listener) {
        ViewUtils.interceptUrlClick(view, listener);
    }

    /**
     * 设置一段字符串的点击事件
     *
     * @param string   字符串段
     * @param color    字符串显示的颜色
     * @param listener 点击监听
     */
    public static void setStringClickable(SpannableString string, int color, View.OnClickListener listener) {
        setStringClickable(string, color, false, listener);
    }

    /**
     * 设置一段字符串的点击事件
     *
     * @param string    字符串段
     * @param color     字符串显示的颜色
     * @param underline 是否显示下划线
     * @param listener  点击监听
     */
    public static void setStringClickable(SpannableString string, int color, boolean underline, View.OnClickListener listener) {
        string.setSpan(new CustomClickSpan(string, color, underline, listener), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 获取字符串长度
     *
     * @param text 字符串
     * @return
     */
    public static int getTextSize(String text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        }
        return text.length();
    }

    /**
     * 分段显示手机号（134 4455 6677）
     *
     * @param phone 手机号
     * @return
     */
    public static String formatPhone(String phone) {
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

    public static String formatPhone1(String phone) {
        String temp = phone.replace(" ","");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < temp.length(); i++){
            sb.append(temp.charAt(i));
            if(i == 2 || i == 6){
                sb.append(" ");
            }
        }
        if(TextUtils.equals(phone,sb.toString())){
            return "";
        }
        return sb.toString();
    }

    /**
     * 分段显示QQ号（123 123 1234）
     *
     * @param QQ
     * @return
     */
    public static String formatQQ(String QQ) {
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
        if ((QQ.length() - 8) > 0 && (QQ.length() - 8) % 5 == 0) {
            if (QQ.endsWith(" ")) {
                return sb.deleteCharAt(sb.length() - 1).toString();
            }
            return sb.insert(QQ.length() - 1, " ").toString();
        }
        return "";
    }

    private static final String REGEX_MOBILE = "^1([3-9])\\d{9}$";

    /**
     * 手机号正则匹配
     *
     * @param phone 手机号
     * @return
     */
    public static boolean isMobilePhone(String phone) {
        if (android.text.TextUtils.isEmpty(phone) || phone.length() < 11) {
            return false;
        }
        if (phone.matches(REGEX_MOBILE)) {
            return true;
        }
        return false;
    }

    private static final String REGEX_IDCARD = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[X|x|0-9]$";

    /**
     * 身份证正则匹配
     *
     * @param idCard 身份证号
     * @return
     */
    public static boolean isIdCard(String idCard) {
        if (TextUtils.isEmpty(idCard) || (idCard.length() != 15 && idCard.length() != 18)) {
            return false;
        }
        if (idCard.matches(REGEX_IDCARD)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是表情符
     * @param codePoint
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
}
