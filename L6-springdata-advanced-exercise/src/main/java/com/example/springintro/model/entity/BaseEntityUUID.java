package com.example.springintro.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntityUUID {

    private String id;


    public BaseEntityUUID() {

    }

    @Id
    @GeneratedValue(generator = "uuid-str")
    @GenericGenerator(name = "uuid-str", strategy = "org.hibernate.id.UUIDGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
