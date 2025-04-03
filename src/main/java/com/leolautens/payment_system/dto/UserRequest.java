package com.leolautens.payment_system.dto;

import com.leolautens.payment_system.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotNull(message = "not null") @NotBlank(message = "not empty") String name,
        @NotNull(message = "not null") @NotBlank(message = "not empty") @Email String email,
        @NotNull(message = "not null") @NotBlank(message = "not empty") String password)
{

    public User toModel() {
        return new User(name, email, password);
    }

}
