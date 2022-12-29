package com.example.dtoexercise.service;


import com.example.dtoexercise.model.dto.UserLoginDto;
import com.example.dtoexercise.model.dto.UserRegisterDto;

public interface UserService {

    void registerUser(UserRegisterDto userRegisterDto);

    void loginUser(UserLoginDto userLoginDto);

    void logOut();

    void gamePurchase(String gameName);

    void getOwnedGames();
}
