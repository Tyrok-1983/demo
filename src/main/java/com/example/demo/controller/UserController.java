package com.example.demo.controller;

import com.example.demo.entity.model.ResponseUser;
import com.example.demo.entity.model.UsersInfoModel;
import com.example.demo.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserInfoService service;

    @GetMapping("/{id}")
    public UsersInfoModel findUserById(@PathVariable Long id) {
        return service.findUserById(id);
    }

    @PutMapping("/create")
    public ResponseUser createUser(@RequestBody UsersInfoModel user,
                                   @RequestHeader(name = "x-Source") String source) {
        if (user == null) {
            log.info("Пользователь для сахранения не может быть null");
            return ResponseUser.builder()
                    .error(true)
                    .description("Пользователь для сахранения не может быть null").build();
        }
        return service.createUser(user, source);
    }

    @PostMapping()
    public List<UsersInfoModel> findUser(@RequestBody UsersInfoModel user){
        return service.findUserInfo(user);
    }
}
