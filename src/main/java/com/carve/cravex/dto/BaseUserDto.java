package com.carve.cravex.dto;

import com.carve.cravex.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public abstract class BaseUserDto {
    @NotBlank(message = "Write Your Email")
    @Email
    private String email;

    @NotBlank(message = "First name can't be empty")
    @Size(min = 3, max = 10 ,message = "Name length should be between 3 and 10")
    private String firstName;

    @NotBlank(message = "Last name can't be empty")
    @Size(min = 3, max = 10 ,message = "Name length should be between 3 and 10")
    private String lastName;

    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, max = 20 , message = "Password length must be greater than 8")
    private String password;

    @NotBlank(message = "Phone Number can't be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Enter a valid phone number")
    private String phone;
}
