package com.example.snkrs.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}
