package com.example.account.domain;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateCommand {
    private String userId;
    private String password;
    @Email
    private String email;
    private Status status;
}
