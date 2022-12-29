package com.example.xmlex.model.dto;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserSeedRootDto {

   @XmlElement(name = "user")
    private List<UserSeedDto> users;

    public UserSeedRootDto() {

    }

    public List<UserSeedDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserSeedDto> users) {
        this.users = users;
    }
}
