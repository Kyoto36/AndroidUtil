package com.ls.comm_util_library;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * @ClassName: RegexInputFilter
 * @Description: 正则过滤器
 *               汉字正则 “[\u4E00-\u9FA5]”，只能输入汉字就要加上非 “[^\u4E00-\u9FA5]”,不然成了不能输入汉字了
 * @Author: ls
 * @Date: 2020/11/19 13:18
 */
public class RegexInputFilter implements InputFilter {
    private String mRegex;

    public RegexInputFilter(String regex){
        mRegex = regex;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        return source.toString().replaceAll(mRegex,"");
    }


}