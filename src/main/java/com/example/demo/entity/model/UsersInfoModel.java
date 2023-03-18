package com.example.demo.entity.model;

import lombok.Data;

@Data
public class UsersInfoModel {
    private Long id;
    private String bankId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String birthDate;
    private String pasNumber;
    private String birthPlace;
    private String phoneNumber;
    private String email;
    private String registrationAddress;
    private String residentialAddress;
}
