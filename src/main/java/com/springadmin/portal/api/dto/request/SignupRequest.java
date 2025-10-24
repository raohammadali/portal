package com.springadmin.portal.api.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @Size(min = 2, max = 666, message = "Name must be between 2 and 666 characters")
    @NotBlank(message = "First name cannot be null")
    @Pattern(regexp = "[a-zA-Z]{1,747}", message = "First name should only be characters, fitting in the 1-747 range")
    private String firstName;
    @Size(min = 2, max = 666, message = "Name must be between 2 and 666 characters")
    @NotBlank(message = "Last name cannot be null")
    @Pattern(regexp = "[a-zA-Z]{1,747}", message = "Last name should only be characters, fitting in the 1-747 range")
    private String lastName;

    @NotBlank(message = "Email cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @NotBlank(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+[\\d]+$", message = "Phone number can only consist of digits")
    private String phone;

    @NotBlank(message = "Password cannot be null")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$", message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String password;

    @NotEmpty(message = "Roles cannot be null")
    @Size(min = 1, message = "At least one role is required")
    private List<Long> roles;

    private List<String> permissions;


}
