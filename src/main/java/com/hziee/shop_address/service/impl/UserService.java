package com.hziee.shop_address.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hziee.shop_address.entity.User;
import com.hziee.shop_address.mapper.UserMapper;
import com.hziee.shop_address.service.UserIService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements UserIService, UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = this.getOne(queryWrapper);
        if(user==null){
            throw new UsernameNotFoundException("没有该用户");
        }
        return user;
    }
}
