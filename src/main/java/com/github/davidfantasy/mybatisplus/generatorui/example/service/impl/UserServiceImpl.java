package com.github.davidfantasy.mybatisplus.generatorui.example.service.impl;

import com.github.davidfantasy.mybatisplus.generatorui.example.entity.User;
import com.github.davidfantasy.mybatisplus.generatorui.example.mapper.UserMapper;
import com.github.davidfantasy.mybatisplus.generatorui.example.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理门户用户表 服务实现类
 * </p>
 *
 * @author david
 * @since 2020-07-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
