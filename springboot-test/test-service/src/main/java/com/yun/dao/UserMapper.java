package com.yun.dao;

import com.yun.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
@CacheConfig(cacheNames = "users")
public interface UserMapper {
    @Insert("insert into user( name , age ) values( #{name} , #{age})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addUser(@Param("name") String uname,@Param("age") String uage);

    @Select("select * from user where id = #{id}")
    @Cacheable(key = "#p0")
    User findById(@Param("id") String uid);

    @Update("update user set name=#{name} where id=#{id}")
    @CachePut(key = "#p0")
    void updataById(@Param("id")String uid,@Param("name")String uname);

    //如果指定为 true，则方法调用后将立即清空所有缓存
    @Delete("delete from user where id=#{id}")
    @CacheEvict(key = "#p0",allEntries = true)
    void deleteById(@Param("id")String id);
}
