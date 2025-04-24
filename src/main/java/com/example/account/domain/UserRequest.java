package com.example.account.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    String userId;
    String password;
    String email;
}
