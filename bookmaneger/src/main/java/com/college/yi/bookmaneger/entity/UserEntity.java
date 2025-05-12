package com.college.yi.bookmaneger.entity;

import lombok.Data;

@Data
public class UserEntity {
    private Long id;
    private String username;
    private String password;
    private String role;
    private Boolean enabled;
}
