package com.example.jsonex.service.impl;

import com.example.jsonex.model.dto.*;
import com.example.jsonex.model.entity.User;
import com.example.jsonex.repository.UserRepository;
import com.example.jsonex.service.UserService;
import com.example.jsonex.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.jsonex.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class UserServiceImpl implements UserService {

    private static final String USERS_FILE_NAME = "users.json";
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public void seedUsers() throws IOException {

        if (this.userRepository.count() > 0) {
            return;
        }

        Arrays.stream(this.gson.fromJson(Files.readString(Path.of(RESOURCES_FILE_PATH + USERS_FILE_NAME)), UserSeedDto[].class))
                .filter(validationUtil::isValid)
                .map(userSeedDto -> modelMapper.map(userSeedDto, User.class))
                .forEach(userRepository::save);

    }

    @Override
    public User findRandomUser() {

        long randomId = ThreadLocalRandom
                .current().nextLong(1, this.userRepository.count() + 1);

        return this.userRepository
                .findById(randomId)
                .orElse(null);
    }

    @Override
    public List<UserSoldDto> findAllUsersWithMoreThanOneSoldProducts() {
        return this.userRepository
                .findAllUsersWithMoreThanOneSoldProductOrderByLastNameThenFirstName()
                .stream()
                .map(user -> modelMapper.map(user, UserSoldDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UsersWithProductWrapperDto findAllUsersAndProducts() {
        List<UserWithProductsDto> dtoList = this.userRepository.findAllUsersWithMoreThanOneSoldProductOrderByCountOfProductsThenLastName()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .map(UserDto::toUserWithProductsDto)
                .collect(Collectors.toList());

        return new UsersWithProductWrapperDto(dtoList);
    }

}
