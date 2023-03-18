package com.example.demo.controller;

import com.example.demo.entity.model.ResponseUser;
import com.example.demo.entity.model.UsersInfoModel;
import com.example.demo.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserInfoService service;
    @InjectMocks
    private UserController controller;

    private static final Long TEST_LONG_PARAM = 1L;
    private static final String MAIL_PROFILE = "mail";

    @Test
    void findUserById() {
        controller.findUserById(TEST_LONG_PARAM);

        verify(service, times(1)).findUserById(anyLong());
    }

    @Test
    void createUser() {
        controller.createUser(new UsersInfoModel(), MAIL_PROFILE);

        verify(service, times(1)).createUser(any(UsersInfoModel.class), anyString());
    }

    @Test
    void createUser_inputUser_Null() {
        final ResponseUser user = controller.createUser(null, MAIL_PROFILE);

        assertNotNull(user);
        assertTrue(user.isError());
    }

    @Test
    void findUser() {
        controller.findUser(new UsersInfoModel());

        verify(service, times(1)).findUserInfo(any(UsersInfoModel.class));
    }
}
