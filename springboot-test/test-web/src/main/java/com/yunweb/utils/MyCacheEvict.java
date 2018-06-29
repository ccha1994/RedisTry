package com.yunweb.utils;

import java.lang.annotation.*;

/**
 * @author wangyunlong
 * @date 2018/6/29 11:22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyCacheEvict {
    /**
     *  缓存key
     */
    String key() default "";
}
