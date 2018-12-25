package com.yun.dao;

import com.yun.domain.User;
import com.yun.util.MyCacheAble;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    @Insert("insert into user( name , age , start) values( #{name} , #{age} , #{start})")
    @Options(useGeneratedKeys = true)
    int addUser(User user);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id);

    @Select("select * from user where id = #{id}")
    User findById2(@Param("id") Integer id);

    @Select("select * from user where 1=1")
    List<User> findAll();

    @Update("update user set name=#{name} where id=#{id}")
    void updataById(@Param("id")Integer id,@Param("name")String name);

    @Delete("delete from user where id=#{id}")
    void deleteById(@Param("id")Integer id);
}
