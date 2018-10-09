package com.gyh.security;

import com.gyh.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author guoyanhong
 * @date 2018/9/28 21:18
 */

@Slf4j
@Service
public class DomainUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("{} want Login", username);
        Map<String, String> userInfo = userDao.queryUserinfoByUsername(username);

        // 创建一个 security 的 User 对象
        List<? extends GrantedAuthority> authorities = new ArrayList();
        User u = new User(username, userInfo.get("password"), authorities);

        return u;
    }
}

