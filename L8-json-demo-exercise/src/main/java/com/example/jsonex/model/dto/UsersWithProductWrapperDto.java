package com.example.jsonex.model.dto;

import java.util.List;

public class UsersWithProductWrapperDto {

    private Integer usersCount;
    private List<UserWithProductsDto> users;

    public UsersWithProductWrapperDto() {

    }

    public UsersWithProductWrapperDto(List<UserWithProductsDto> users) {
        this.users = users;
        this.usersCount = users.size();
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public List<UserWithProductsDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserWithProductsDto> users) {
        this.users = users;
    }
}
