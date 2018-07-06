package com.yun.service;

import com.yun.dao.UserMapper;
import com.yun.domain.User;
import com.yun.util.MyCacheAble;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class RedisService {

    @Resource
    private UserMapper userMapper;

    public int addUser(User user) {

        return userMapper.addUser(user);
    }

    public User findById(Integer ids) {
        return userMapper.findById(ids);
    }

    public User findById2(Integer ids) {
        return userMapper.findById2(ids);
    }

    public List<User> findAll() {
        return userMapper.findAll();
    }

    public void updataById(Integer ids, String names) {

        userMapper.updataById(ids, names);
    }

    public void deleteById(Integer ids) {

        userMapper.deleteById(ids);
    }

}
