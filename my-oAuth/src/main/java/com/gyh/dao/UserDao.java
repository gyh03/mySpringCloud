package com.gyh.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserDao {


    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @Select("SELECT username,password FROM `yfs_user` u WHERE u.`username`=#{username}")
    Map<String,String> queryUserinfoByUsername(@Param("username") String username);

}
