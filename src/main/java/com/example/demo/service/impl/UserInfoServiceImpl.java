package com.example.demo.service.impl;

import com.example.demo.entity.UsersInfo;
import com.example.demo.entity.model.ResponseUser;
import com.example.demo.entity.model.UsersInfoModel;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository repository;

    private static final String MAIL_PROFILE = "mail";
    private static final String MOBILE_PROFILE = "mobile";
    private static final String BANK_PROFILE = "bank";
    private static final String GOSUSLUGI_PROFILE = "gosuslugi";

    @Override
    @Transactional(readOnly = true)
    public UsersInfoModel findUserById(Long id) {
        final UsersInfo usersInfo = repository.findById(id).orElse(null);
        final UsersInfoModel target = new UsersInfoModel();
        if (usersInfo != null) {
            BeanUtils.copyProperties(usersInfo, target);
        }
        return target;
    }

    @Override
    @Transactional
    public ResponseUser createUser(UsersInfoModel user, String source) {
        final UsersInfo usersInfo = new UsersInfo();
        final ResponseUser.ResponseUserBuilder builder = ResponseUser.builder();

        checkUserFromProfile(user, source, builder);
        final ResponseUser build = builder.build();
        if (build.isError()) {
            return build;
        }
        BeanUtils.copyProperties(user, usersInfo);
        try {
            repository.save(usersInfo);
        } catch (RuntimeException e) {
            return build.toBuilder().description(e.getMessage()).build();
        }
        return build.toBuilder().description("Пользователь успешно сохранен").build();
    }

    @Override
    @Transactional
    public List<UsersInfoModel> findUserInfo(UsersInfoModel user) {
        List<UsersInfoModel> result = new ArrayList<>();
        final String phoneNumber = user.getPhoneNumber();
        try {
            final List<UsersInfo> users = repository.findUsersInfo(user.getFirstName(), user.getMiddleName(),
                    user.getMiddleName(), phoneNumber != null ? normalizePhoneNumber(user) : phoneNumber, user.getEmail());
            for (UsersInfo usersInfo : users) {
                final UsersInfoModel temp = new UsersInfoModel();
                BeanUtils.copyProperties(usersInfo, temp);
                result.add(temp);
            }
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
        return result;
    }

    private String normalizePhoneNumber(UsersInfoModel user) {
        return user.getPhoneNumber().replaceAll("[+ ()-]", "");
    }

    private void checkUserFromProfile(UsersInfoModel user, String source, ResponseUser.ResponseUserBuilder builder) {
        switch (source) {
            case MAIL_PROFILE:
                if (StringUtils.isBlank(user.getEmail()) || StringUtils.isBlank(user.getFirstName())) {
                    builder.error(true)
                            .description("Для профили mail поля Email и FirstName обязательны");
                }
                break;
            case MOBILE_PROFILE:
                if (StringUtils.isBlank(user.getPhoneNumber())) {
                    builder.error(true)
                            .description("Для профили mobile поля PhoneNumber обязательны");
                } else {
                    validationPhoneNumber(user, builder);
                }
                break;
            case BANK_PROFILE:
                if (ObjectUtils.anyNull(user.getBankId(), user.getLastName(), user.getFirstName(), user.getMiddleName(),
                        user.getBirthDate(), user.getPasNumber())) {
                    builder.error(true)
                            .description("Для профили bank поля BankId, LastName, FirstName, MiddleName, BirthDate, PasNumber обязательны");
                } else {
                    validatePassNumber(user, builder);
                }

                break;
            case GOSUSLUGI_PROFILE:
                if (ObjectUtils.anyNull(user.getBankId(), user.getLastName(), user.getFirstName(), user.getMiddleName(),
                        user.getBirthDate(), user.getPasNumber(), user.getPhoneNumber(), user.getBirthPlace(), user.getRegistrationAddress())) {
                    builder.error(true)
                            .description("Для профили gosuslugi все поля кроме email и residentialAddress обязательны");
                } else {
                    validationPhoneNumber(user, builder);
                    validatePassNumber(user, builder);
                }
                break;
            default:
                builder.error(true)
                        .description("Данные получены из неизвестного источника. Пользователь не может быть сохранен");
        }
    }

    private void validatePassNumber(UsersInfoModel user, ResponseUser.ResponseUserBuilder builder) {
        if (user.getPasNumber() != null) {
            final String pasNumber = user.getPasNumber().replaceAll(" ", "");
            if (!pasNumber.matches("\\d{1,}")) {
                builder.error(true)
                        .description("Номер и серия паспорта должны состоять только из цифр")
                        .build();
            }
            if (pasNumber.length() != 10) {
                builder.error(true)
                        .description("Паспорта и серия не соответствую требуемой длине 10 цифр");
            }
            final ResponseUser build = builder.build();
            if (!build.isError()) {
                user.setPasNumber(pasNumber.substring(0, 3) + " " + pasNumber.substring(3, 10));
            }
        }
    }

    private void validationPhoneNumber(UsersInfoModel user, ResponseUser.ResponseUserBuilder builder) {
        if (user.getPhoneNumber() != null) {
            final String normalizeNumber = normalizePhoneNumber(user);
            if (normalizeNumber.indexOf("7") != 0) {
                builder.error(true)
                        .description("В номере телефона первой цифрой должны быть 7");
            } else if (normalizeNumber.length() != 11) {
                builder.error(true)
                        .description("Номер телефона не соответствую требуемой длине")
                        .build();
            } else if (!normalizeNumber.matches("\\d{11}")) {
                builder.error(true)
                        .description("Номер телефона телефона должен состоять только из цифр")
                        .build();
            }
            final ResponseUser build = builder.build();
            if (!build.isError()) {
                user.setPhoneNumber(normalizeNumber);
            }
        }
    }
}
