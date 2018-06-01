package com.yun.service;

import com.yun.dao.UserMapper;
import com.yun.domain.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class RedisService    {

    @Resource
    private UserMapper userMapper;

    public int addUser(String names,String ages){
        return userMapper.addUser(names,ages);
    }

    public User findById(String ids){
        return userMapper.findById(ids);
    }

    public void updataById(String ids,String names){
        userMapper.updataById(ids,names);
    }

    public void deleteById(String ids){
        userMapper.deleteById(ids);
    }

}
