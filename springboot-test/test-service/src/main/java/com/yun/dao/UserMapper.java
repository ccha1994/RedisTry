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
@CacheConfig(cacheNames = "users")
public interface UserMapper {

    @Insert("insert into user( name , age ) values( #{name} , #{age})")
    @CacheEvict(key="#p0")
    @Options(useGeneratedKeys = true)
    int addUser(User user);

    @Select("select * from user where id = #{id}")
    //@Cacheable(key="#p0")
    User findById(@Param("id") Integer id);

    @Select("select * from user where id = #{id}")
    @MyCacheAble(key = "'userCache:userId.' + #id")
    User findById2(@Param("id") Integer id);

    @Select("select * from user where 1=1")
    @Cacheable
    List<User> findAll();

    @Update("update user set name=#{name} where id=#{id}")
    @CacheEvict(key="#p0")
    void updataById(@Param("id")Integer id,@Param("name")String name);

    @Delete("delete from user where id=#{id}")
    @CacheEvict(key="#p0")
    void deleteById(@Param("id")Integer id);
}
