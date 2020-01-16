package com.wxzd.gfzdj.global.base;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义注解---在同一个Activity中保证同一个对象只被实例化一次
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface PerActivity {
}
