package com.example.demo.repository;

import com.example.demo.entity.UsersInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends JpaRepository<UsersInfo, Long> {

    @Query("SELECT u FROM UsersInfo u WHERE (:firstName is null or u.firstName = :firstName) and (:middleName is null"
            + " or u.middleName = :middleName) and (:lastName is null or u.lastName = :lastName)"
            + " and (:phoneNumber is null or u.phoneNumber = :phoneNumber) and (:email is null or u.email = :email)")
    List<UsersInfo> findUsersInfo(@Param("firstName")String firstName, @Param("middleName")String middleName,
                                  @Param("lastName")String lastName, @Param("phoneNumber")String phoneNumber,
                                  @Param("email")String email);
}
