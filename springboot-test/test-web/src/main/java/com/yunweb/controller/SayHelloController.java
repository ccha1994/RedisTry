package com.yunweb.controller;

import com.yun.service.RedisService;
import com.yun.service.SayHelloService;
import com.yun.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SayHelloController {
    @Resource
    private SayHelloService sayHelloService;

    @Resource RedisService redisService;

    @RequestMapping("/hello")
    public String sayHello(){
        return sayHelloService.sayhello();
    }

    @RequestMapping("/addUser")
    public int addUser(@RequestParam("name")String name, @RequestParam("age")String age){
        return redisService.addUser(name, age);
    }

    @RequestMapping("/findUser")
    public User findUser(@RequestParam("id") String id){

        return redisService.findById(Integer.parseInt(id));
    }

    @RequestMapping("/updataById")
    public String updataById(@RequestParam("id") String id,@RequestParam("name") String name){
        try {
            redisService.updataById(Integer.parseInt(id), name);
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

    @RequestMapping("/deleteById")
    public String deleteById(@RequestParam("id") String id){
        try {
            redisService.deleteById(Integer.parseInt(id));
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }
}
