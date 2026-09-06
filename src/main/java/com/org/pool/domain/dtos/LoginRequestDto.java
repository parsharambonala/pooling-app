package com.org.pool.domain.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "provide mail id")
    private String mailId;

    @NotBlank(message = "provide password")
    private String password;

}
