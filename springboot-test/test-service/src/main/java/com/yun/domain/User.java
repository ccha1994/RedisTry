package com.yun.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
public class User implements Serializable {
    Integer id;
    String name;
    String age;
    Date start;

    public User() {
    }

    public User(Integer id, String name, String age, Date start) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.start = start;
    }
}
