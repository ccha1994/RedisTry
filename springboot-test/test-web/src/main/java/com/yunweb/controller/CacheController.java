package com.yunweb.controller;

import com.yunweb.utils.Cache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangyunlong
 * @date 2018/6/29 16:44
 */
@RestController
public class CacheController {
    @Resource
    private Cache cache;

    @RequestMapping("/cache")
    public String showCache() {
        return cache.listAll();
    }

}
