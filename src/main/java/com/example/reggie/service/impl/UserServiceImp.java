package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.entity.User;
import com.example.reggie.mapper.UserMapper;
import com.example.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service

public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {

}
