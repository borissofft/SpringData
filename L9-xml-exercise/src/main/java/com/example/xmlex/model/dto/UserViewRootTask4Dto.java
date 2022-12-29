package com.example.xmlex.model.dto;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserViewRootTask4Dto {
    @XmlAttribute(name = "count")
    private Integer count;

    @XmlElement(name = "user")
    private List<UserNamesAndAgeDto> users;

    public UserViewRootTask4Dto() {

    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<UserNamesAndAgeDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserNamesAndAgeDto> users) {
        this.users = users;
    }
}
