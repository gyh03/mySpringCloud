package com.gyh.security;

import com.gyh.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service
public class DomainUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("{} want Login", username);
        List<? extends GrantedAuthority> authorities = new ArrayList();
        Map<String, String> userInfo = userDao.queryUserinfoByMobile(username);

        User u = new User(username, userInfo.get("password"), authorities);

        return u;
    }
}
