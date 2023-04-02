package com.example.snkrs.model;

import lombok.Data;

@Data
public class Address {
    private String firstName;
    private String lastName;
    private String address;
    private Integer province;
    private Integer district;
    private Integer ward;
    private String phone;
    private String email;
}
