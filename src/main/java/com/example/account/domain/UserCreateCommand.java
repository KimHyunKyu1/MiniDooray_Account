package com.example.account.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateCommand {
    private String userId;
    private String password;
    private String email;
    private Status status;
}
