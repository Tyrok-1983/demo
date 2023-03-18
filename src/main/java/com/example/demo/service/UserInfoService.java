package com.example.demo.service;

import com.example.demo.entity.model.ResponseUser;
import com.example.demo.entity.model.UsersInfoModel;

import java.util.List;

public interface UserInfoService {

    UsersInfoModel findUserById(Long id);

    ResponseUser createUser(UsersInfoModel user, String source);

    List<UsersInfoModel> findUserInfo(UsersInfoModel user);
}
