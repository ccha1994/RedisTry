package com.yunweb.utils;

import java.lang.annotation.*;

/**
 * @author wangyunlong
 * @date 2018/6/29 11:20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyCacheAble {
    /**
     *  缓存key
     */
    String key() default "";
}
