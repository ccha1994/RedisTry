package com.yunweb.util;

import java.lang.annotation.*;
import java.util.HashMap;

/**
 * @author wangyunlong
 * @date 2018/7/4 16:48
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface annotest {
    String name() default "wyl";
    String email() default "wyl@163.com";
}
