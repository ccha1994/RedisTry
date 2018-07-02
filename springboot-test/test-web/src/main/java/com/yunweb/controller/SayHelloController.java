package com.yunweb.controller;

import com.yun.service.RedisService;
import com.yun.service.SayHelloService;
import com.yun.domain.User;
import com.yun.util.MyCacheAble;
import com.yun.util.MyCacheEvict;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        User user = new User();
        user.setName(name);
        user.setAge(age);
        redisService.addUser(user);
        int id = user.getId();
        return id;
    }

    @RequestMapping("/findUser")
    @MyCacheAble(key = "'userCache:userId.' + #id")
    public User findUser(@RequestParam("id") String id){

        return redisService.findById(Integer.parseInt(id));
    }

    @RequestMapping("/findAll")
    public List<User> findUser(){

        return redisService.findAll();
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
    @MyCacheEvict(key = "'userCache:userId.' + #id")
    public String deleteById(@RequestParam("id") String id){
        try {
            redisService.deleteById(Integer.parseInt(id));
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

}
