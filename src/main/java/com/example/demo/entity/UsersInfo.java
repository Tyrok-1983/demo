package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_info")
public class UsersInfo {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "bank_id")
    private String bankId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birth_date")
    private String birthDate;
    @Column(name = "pas_number")
    private String pasNumber;
    @Column(name = "birth_place")
    private String birthPlace;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;
    @Column(name = "registration_address")
    private String registrationAddress;
    @Column(name = "residential_address")
    private String residentialAddress;
}
