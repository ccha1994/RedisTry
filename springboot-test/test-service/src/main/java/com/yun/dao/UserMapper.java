package com.yun.dao;

import com.yun.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
@CacheConfig(cacheNames = "users")
public interface UserMapper {
    @Insert("insert into user( name , age ) values( #{name} , #{age})")
    @CacheEvict(allEntries = true)
    int addUser(@Param("name") String name,@Param("age") String age);

    @Select("select * from user where id = #{id}")
    @Cacheable(key = "#p0")
    User findById(@Param("id") Integer id);

    @Select("select * from user where 1=1")
    @Cacheable
    List<User> findAll();

    @Update("update user set name=#{name} where id=#{id}")
    @CacheEvict(key = "#p0")
    void updataById(@Param("id")Integer uid,@Param("name")String uname);

    @Delete("delete from user where id=#{id}")
    @CacheEvict(key = "#p0")
    void deleteById(@Param("id")Integer id);
}
