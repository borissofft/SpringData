package com.example.xmlex.service.impl;

import com.example.xmlex.model.dto.*;
import com.example.xmlex.model.entity.User;
import com.example.xmlex.repository.UserRepository;
import com.example.xmlex.service.UserService;
import com.example.xmlex.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }


    @Override
    public long getEntityCount() {
        return this.userRepository.count();
    }

    @Override
    public void seedUsers(List<UserSeedDto> users) {
        users.stream()
                .filter(validationUtil::isValid)
                .map(userSeedDto -> modelMapper.map(userSeedDto, User.class))
                .forEach(userRepository::save);
    }

    @Override
    public User getRandomUser() {

        long randomId = ThreadLocalRandom.current()
                .nextLong(1, this.userRepository.count() + 1);
        return this.userRepository
                .findById(randomId)
                .orElse(null);
    }

    @Override
    public UserViewRootDto findUsersWithMoreThenOneSoldProduct() {
        UserViewRootDto userViewRootDto = new UserViewRootDto();

        userViewRootDto
                .setUsers(this.userRepository.findAllUsersWithMoreThanOneSoldProduct()
                        .stream()
                        .map(user -> modelMapper.map(user, UserWithProductsDto.class))
                        .collect(Collectors.toList()));

        return userViewRootDto;
    }

    @Override
    public UserViewRootTask4Dto findUsersWithMoreThenOneSoldProductOrderBySoldProducts() {
        UserViewRootTask4Dto task4Dto = new UserViewRootTask4Dto();
        task4Dto.setUsers(this.userRepository.findAllUsersWithMoreThanOneSoldProductOrderBySoldProducts()
                .stream()
                .map(user -> {
                    UserNamesAndAgeDto namesAndAgeDto = modelMapper.map(user, UserNamesAndAgeDto.class);

                    SoldProductsDto soldProductsDto = new SoldProductsDto();
                    List<ProductNamePriceDto> productNamePriceDtos = user.getSoldProducts().stream()
                            .map(product -> modelMapper.map(product, ProductNamePriceDto.class)).toList();

                    soldProductsDto.setCount(user.getSoldProducts().size());
                    soldProductsDto.setProducts(productNamePriceDtos);

                    namesAndAgeDto.setSoldProducts(soldProductsDto);

                    return namesAndAgeDto;
                })
                .collect(Collectors.toList()));

        task4Dto.setCount(task4Dto.getUsers().size());
        return task4Dto;
    }

}
