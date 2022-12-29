package com.example.xmlex.service;

import com.example.xmlex.model.dto.UserSeedDto;
import com.example.xmlex.model.dto.UserViewRootDto;
import com.example.xmlex.model.dto.UserViewRootTask4Dto;
import com.example.xmlex.model.entity.User;

import java.util.List;

public interface UserService {
    long getEntityCount();
    void seedUsers(List<UserSeedDto> users);

    User getRandomUser();

    UserViewRootDto findUsersWithMoreThenOneSoldProduct();

    UserViewRootTask4Dto findUsersWithMoreThenOneSoldProductOrderBySoldProducts();
}
