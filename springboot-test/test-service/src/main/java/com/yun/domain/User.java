package com.yun.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    Integer id;
    String name;
    String age;
}
