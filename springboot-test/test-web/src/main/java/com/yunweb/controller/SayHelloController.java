package com.yunweb.controller;

import com.yun.service.RedisService;
import com.yun.service.SayHelloService;
import com.yun.domain.User;
import com.yun.util.MyCacheAble;
import com.yun.util.MyCacheEvict;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CacheConfig(cacheNames = {"userCache"})
public class SayHelloController {
    @Resource
    private SayHelloService sayHelloService;

    @Resource RedisService redisService;


    @RequestMapping("/hello")
    public String sayHello(){
        return sayHelloService.sayhello();
    }

    @RequestMapping("/addUser")
    @CachePut(key = "'userId.' + #user.getId()")
    public User addUser(User user){
        user.setStart(new Date());
        redisService.addUser(user);
        return user;
    }

    @RequestMapping("/findUser")
    @Cacheable(key = "'userId.' + #id")
    public User findUser(@RequestParam("id") String id){
        System.out.println("数据库查询");
        return redisService.findById(Integer.parseInt(id));
    }

    @RequestMapping("/findUser2")
    public User findUser2(@RequestParam("id") String id){
        return redisService.findById2(Integer.parseInt(id));
    }

    @RequestMapping("/findAll")
    public List<User> findUser(){
        return redisService.findAll();
    }

    @RequestMapping("/updataById")
    @CacheEvict(key = "'userId.' + #id")
    public String updataById(@RequestParam("id") String id,@RequestParam("name") String name){
        try {
            redisService.updataById(Integer.parseInt(id), name);
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

    @RequestMapping("/deleteById")
    @CacheEvict(key = "'userId.' + #id")
    public String deleteById(@RequestParam("id") String id){
        try {
            redisService.deleteById(Integer.parseInt(id));
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

}
