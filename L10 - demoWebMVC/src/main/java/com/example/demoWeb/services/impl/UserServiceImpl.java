package com.example.demoWeb.services.impl;

import com.example.demoWeb.dto.UserLoginDto;
import com.example.demoWeb.dto.UserRegisterDto;
import com.example.demoWeb.entity.User;
import com.example.demoWeb.repositories.UserRepository;
import com.example.demoWeb.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean register(UserRegisterDto userRequest) {

        if (this.userRepository.existsByUsernameOrEmail(userRequest.getUsername(), userRequest.getEmail())) {
            return false;
        }

        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            return false;
        }

        User user = this.modelMapper.map(userRequest, User.class);
        this.userRepository.save(user);

        return true;
    }

    @Override
    public Long validateUserLoginDetails(UserLoginDto userRequest) {

        User user = this.userRepository.findFirstByUsername(userRequest.getUsername()).orElse(null);

        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(userRequest.getPassword())) {
            return null;
        }

        return user.getId();
    }

}
