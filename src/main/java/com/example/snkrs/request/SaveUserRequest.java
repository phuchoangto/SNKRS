package com.example.snkrs.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SaveUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "Password must contain at least one digit, one lower case letter, one upper case letter, one special character, no whitespace, and at least 8 characters long")
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String roles;
}
