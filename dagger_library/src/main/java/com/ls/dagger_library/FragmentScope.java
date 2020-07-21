package com.ls.dagger_library;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
* @FileName: ActivityScope.java
* @Description: 与activity生命周期相同的注解
* @Author: ls
* @Date: 2019/7/22 14:46
*/
@Scope
@Documented
@Retention(RUNTIME)
public @interface FragmentScope {
}
