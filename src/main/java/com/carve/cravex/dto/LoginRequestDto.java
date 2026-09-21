package com.carve.cravex.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @Email(message = "Enter valid email")
    private String email;

    @NotBlank(message = "Password can't be empty")
    private String password;
}
