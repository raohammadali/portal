package com.springadmin.portal.api.dto.request;
import java.util.List;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateRequest {
    @NotBlank(message = "First name cannot be null")
    @Pattern(regexp = "[a-zA-Z]{1,747}", message = "First name should only be characters, fitting in the 1-747 range")
    private String firstName;

    @NotBlank(message = "Last name cannot be null")
    @Pattern(regexp = "[a-zA-Z]{1,747}", message = "Last name should only be characters, fitting in the 1-747 range")
    private String lastName;

    @NotBlank(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+[\\d]+$", message = "Phone number can only consist of digits")
    private String phone;

    private String profileImageUrl;

    @NotBlank(message = "Email cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @NotEmpty(message = "Roles cannot be null")
    @Size(min = 1, message = "At least one role is required")
    private List<String> roles;
    private List<String> permissions;

}
