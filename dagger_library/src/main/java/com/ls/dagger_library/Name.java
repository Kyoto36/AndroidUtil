package com.ls.dagger_library;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
* @FileName: Name.java
* @Description: dagger inject不同对象相同返回值的方法的唯一标识限定符
* @Author: ls
* @Date: 2019/7/22 14:52
*/
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    String name() default "default";
}