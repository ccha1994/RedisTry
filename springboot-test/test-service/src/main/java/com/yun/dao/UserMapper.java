package com.yun.dao;

import com.yun.domain.User;

import com.yun.util.MyCacheAble;
import com.yun.util.MyCacheEvict;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.*;
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

    @Select("select * from user where 1=1")
    @Cacheable
    List<User> findAll();

    @Update("update user set name=#{name} where id=#{id}")
    @CacheEvict(key="#p0")
    void updataById(@Param("id")Integer id,@Param("name")String name);

    /**
     * 如果指定为 true，则方法调用后将立即清空所有缓存
     */
    @Delete("delete from user where id=#{id}")
    //@CacheEvict(allEntries = true)
    void deleteById(@Param("id")Integer id);
}
