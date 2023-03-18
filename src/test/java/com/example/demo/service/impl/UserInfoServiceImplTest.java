package com.example.demo.service.impl;

import com.example.demo.entity.UsersInfo;
import com.example.demo.entity.model.ResponseUser;
import com.example.demo.entity.model.UsersInfoModel;
import com.example.demo.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceImplTest {
    @Mock
    private UserInfoRepository repository;
    @InjectMocks
    private UserInfoServiceImpl service;

    private static final Long TEST_LONG_PARAM = 1L;
    private static final String STRING_TEST_PARAM = "TEST";
    private static final String MAIL_PROFILE = "mail";
    private static final String MOBILE_PROFILE = "mobile";
    private static final String BANK_PROFILE = "bank";
    private static final String GOSUSLUGI_PROFILE = "gosuslugi";

    @Test
    void findUserById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(mock(UsersInfo.class, RETURNS_DEEP_STUBS)));

        final UsersInfoModel userById = service.findUserById(TEST_LONG_PARAM);

        assertNotNull(userById);
        verify(repository, atLeastOnce()).findById(anyLong());
    }

    @Test
    void createUser() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setPhoneNumber("7 (183) 456-75-23");
        usersInfoModel.setPasNumber("9856 567891");
        usersInfoModel.setEmail(STRING_TEST_PARAM);
        usersInfoModel.setFirstName(STRING_TEST_PARAM);

        final ResponseUser user = service.createUser(usersInfoModel, MAIL_PROFILE);

        assertNotNull(user);
        assertFalse(user.isError());
        assertEquals("Пользователь успешно сохранен", user.getDescription());
        verify(repository, atLeastOnce()).save(any(UsersInfo.class));
    }

    @Test
    void createUser_PasNumber_ContainsLetter() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setBankId(STRING_TEST_PARAM);
        usersInfoModel.setPasNumber("9856 56789d");
        usersInfoModel.setFirstName(STRING_TEST_PARAM);
        usersInfoModel.setLastName(STRING_TEST_PARAM);
        usersInfoModel.setMiddleName(STRING_TEST_PARAM);
        usersInfoModel.setBirthDate(STRING_TEST_PARAM);

        final ResponseUser user = service.createUser(usersInfoModel, BANK_PROFILE);

        assertNotNull(user);
        assertTrue(user.isError());
        assertEquals("Номер и серия паспорта должны состоять только из цифр", user.getDescription());
        verify(repository, never()).save(any(UsersInfo.class));
    }

    @Test
    void createUser_PasNumber_Ok() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setBankId(STRING_TEST_PARAM);
        usersInfoModel.setPasNumber("9856 567897");
        usersInfoModel.setFirstName(STRING_TEST_PARAM);
        usersInfoModel.setLastName(STRING_TEST_PARAM);
        usersInfoModel.setMiddleName(STRING_TEST_PARAM);
        usersInfoModel.setBirthDate(STRING_TEST_PARAM);

        final ResponseUser user = service.createUser(usersInfoModel, BANK_PROFILE);

        assertNotNull(user);
        assertFalse(user.isError());
        assertEquals("Пользователь успешно сохранен", user.getDescription());
        verify(repository, atLeastOnce()).save(any(UsersInfo.class));
    }

    @Test
    void createUser_PasNumber_NotValidLength() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setBankId(STRING_TEST_PARAM);
        usersInfoModel.setPasNumber("9856 56789987");
        usersInfoModel.setFirstName(STRING_TEST_PARAM);
        usersInfoModel.setLastName(STRING_TEST_PARAM);
        usersInfoModel.setMiddleName(STRING_TEST_PARAM);
        usersInfoModel.setBirthDate(STRING_TEST_PARAM);

        final ResponseUser user = service.createUser(usersInfoModel, BANK_PROFILE);

        assertNotNull(user);
        assertTrue(user.isError());
        assertEquals("Паспорта и серия не соответствую требуемой длине 10 цифр", user.getDescription());
        verify(repository, never()).save(any(UsersInfo.class));
    }

    @Test
    void createUser_PhoneNumber_ContainsLetter() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setPhoneNumber("7 (183) 456-75-2j");

        final ResponseUser user = service.createUser(usersInfoModel, MOBILE_PROFILE);

        assertNotNull(user);
        assertTrue(user.isError());
        assertEquals("Номер телефона телефона должен состоять только из цифр", user.getDescription());
        verify(repository, never()).save(any(UsersInfo.class));
    }

    @Test
    void createUser_PhoneNumber_NotValidFirstElement() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setPhoneNumber("5 (183) 456-75-24");

        final ResponseUser user = service.createUser(usersInfoModel, MOBILE_PROFILE);

        assertNotNull(user);
        assertTrue(user.isError());
        assertEquals("В номере телефона первой цифрой должны быть 7", user.getDescription());
        verify(repository, never()).save(any(UsersInfo.class));
    }

    @Test
    void createUser_PhoneNumber_NotValidLength() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setPhoneNumber("7 (183) 456-75-242");

        final ResponseUser user = service.createUser(usersInfoModel, MOBILE_PROFILE);

        assertNotNull(user);
        assertTrue(user.isError());
        assertEquals("Номер телефона не соответствую требуемой длине", user.getDescription());
        verify(repository, never()).save(any(UsersInfo.class));
    }

    @Test
    void createUser_mailProfil_notValid() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setPhoneNumber("7 (183) 456-75-23");
        usersInfoModel.setPasNumber("9856 567891");
        usersInfoModel.setEmail(STRING_TEST_PARAM);

        final ResponseUser user = service.createUser(usersInfoModel, MAIL_PROFILE);

        assertNotNull(user);
        assertTrue(user.isError());
        assertEquals("Для профили mail поля Email и FirstName обязательны", user.getDescription());
        verify(repository, never()).save(any(UsersInfo.class));
    }

    @Test
    void createUser_mobileProfil_notValid() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setPasNumber("9856 567891");
        usersInfoModel.setEmail(STRING_TEST_PARAM);

        final ResponseUser user = service.createUser(usersInfoModel, MOBILE_PROFILE);

        assertNotNull(user);
        assertTrue(user.isError());
        assertEquals("Для профили mobile поля PhoneNumber обязательны", user.getDescription());
        verify(repository, never()).save(any(UsersInfo.class));
    }

    @Test
    void createUser_bankProfil_notValid() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setPasNumber("9856 567891");
        usersInfoModel.setEmail(STRING_TEST_PARAM);

        final ResponseUser user = service.createUser(usersInfoModel, BANK_PROFILE);

        assertNotNull(user);
        assertTrue(user.isError());
        assertEquals("Для профили bank поля BankId, LastName, FirstName, MiddleName, BirthDate, PasNumber обязательны", user.getDescription());
        verify(repository, never()).save(any(UsersInfo.class));
    }

    @Test
    void createUser_gosuslugiProfil_notValid() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setPasNumber("9856 567891");
        usersInfoModel.setEmail(STRING_TEST_PARAM);

        final ResponseUser user = service.createUser(usersInfoModel, GOSUSLUGI_PROFILE);

        assertNotNull(user);
        assertTrue(user.isError());
        assertEquals("Для профили gosuslugi все поля кроме email и residentialAddress обязательны", user.getDescription());
        verify(repository, never()).save(any(UsersInfo.class));
    }

    @Test
    void createUser_gosuslugiProfil_notValidPassNum() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setBankId(STRING_TEST_PARAM);
        usersInfoModel.setPhoneNumber("7 (183) 456-75-23");
        usersInfoModel.setPasNumber("9856 5678999");
        usersInfoModel.setFirstName(STRING_TEST_PARAM);
        usersInfoModel.setLastName(STRING_TEST_PARAM);
        usersInfoModel.setMiddleName(STRING_TEST_PARAM);
        usersInfoModel.setBirthDate(STRING_TEST_PARAM);
        usersInfoModel.setBirthPlace(STRING_TEST_PARAM);
        usersInfoModel.setRegistrationAddress(STRING_TEST_PARAM);

        final ResponseUser user = service.createUser(usersInfoModel, GOSUSLUGI_PROFILE);

        assertNotNull(user);
        assertTrue(user.isError());
        assertEquals("Паспорта и серия не соответствую требуемой длине 10 цифр", user.getDescription());
        verify(repository, never()).save(any(UsersInfo.class));
    }

    @Test
    void createUser_unknownProfil_notValid() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setPasNumber("9856 567891");
        usersInfoModel.setEmail(STRING_TEST_PARAM);

        final ResponseUser user = service.createUser(usersInfoModel, STRING_TEST_PARAM);

        assertNotNull(user);
        assertTrue(user.isError());
        assertEquals("Данные получены из неизвестного источника. Пользователь не может быть сохранен", user.getDescription());
        verify(repository, never()).save(any(UsersInfo.class));
    }

    @Test
    void findUserInfo() {
        final UsersInfoModel usersInfoModel = new UsersInfoModel();
        usersInfoModel.setPhoneNumber("7 (183) 456-75-242");
        when(repository.findUsersInfo(any(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(mock(UsersInfo.class, RETURNS_DEEP_STUBS)));

        final List<UsersInfoModel> users = service.findUserInfo(usersInfoModel);

        assertNotNull(users);
        assertEquals(1, users.size());
        verify(repository, times(1)).findUsersInfo(any(), any(), any(), any(), any());
    }
}