package com.yun.service;

import org.springframework.stereotype.Service;

@Service
public class SayHelloService {
    public String sayhello(){
        return "hello world!";
    }
}
