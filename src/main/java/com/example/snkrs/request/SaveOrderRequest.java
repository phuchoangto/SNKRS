package com.example.snkrs.request;

import com.example.snkrs.model.PaymentMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SaveOrderRequest {
    @NotBlank(message = "Please enter your first name")
    private String firstName;

    @NotBlank(message = "Please enter your last name")
    private String lastName;

    @Email
    private String email;

    @NotNull(message = "Please enter your phone number")
    @Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})\\b$", message = "Phone number is invalid")
    private String phone;

    @NotBlank(message = "Please enter your address")
    private String address;

    @NotNull(message = "Please choose your ward")
    private Integer ward;

    @NotNull(message = "Please choose your district")
    private Integer district;

    @NotNull(message = "Please choose your province")
    private Integer province;

    private Boolean saveInfo = false;
    private String note;

    @NotNull(message = "Please choose your payment method")
    private PaymentMethod paymentMethod;
}
