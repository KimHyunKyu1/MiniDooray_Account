package com.example.account.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRequest {
    private String userId;
    private String password;

}