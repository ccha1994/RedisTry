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
    //@Options(useGeneratedKeys = true, keyProperty = "id")
    int addUser(@Param("name") String name,@Param("age") String age);

    @Select("select * from user where id = #{uid}")
    @Cacheable(key = "#p0")
    User findById(@Param("uid") Integer id);

    @Select("select * from user where 1=1")
    List<User> findAll();

    @Update("update user set name=#{name} where id=#{id}")
    @CachePut(key = "#p0")
    void updataById(@Param("id")Integer uid,@Param("name")String uname);

    //如果指定为 true，则方法调用后将立即清空所有缓存
    @Delete("delete from user where id=#{id}")
    @CacheEvict(key = "#p0",allEntries = true)
    void deleteById(@Param("id")Integer id);
}
