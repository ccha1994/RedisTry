package com.yunweb.controller;

import com.yunweb.util.annotest;

/**
 * @author wangyunlong
 * @date 2018/7/4 16:49
 */
public class AnnoTestController {
    @annotest(name = "wyl0")
    private static String name;

    @annotest(name = "wyl1@163.com")
    private static String email;

    public static void main(String[] args) {
        System.out.println("name:"+name+" email:"+email);
    }
}
