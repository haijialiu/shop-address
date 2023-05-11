package com.hziee.shop_address.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hziee.shop_address.entity.User;
import com.hziee.shop_address.mapper.UserMapper;
import com.hziee.shop_address.service.UserIService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements UserIService {
}
