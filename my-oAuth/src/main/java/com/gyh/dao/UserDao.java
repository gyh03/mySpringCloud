package com.gyh.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserDao {


    @Select("SELECT username,password FROM `yfs_user` u WHERE u.`username`=#{username}")
    Map<String,String> queryUserinfoByMobile(@Param("username") String username);

}
