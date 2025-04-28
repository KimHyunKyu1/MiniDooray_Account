package com.example.account.service;

import com.example.account.domain.*;

public interface UserService {
    public UserView registerUser(UserCreateCommand userCreateCommand);


    public void deleteUser(String userId);

    public UserView updateUser(UserUpdateRequest userUpdateRequest);

    public UserLoginRequest matchUserLoginRequest(UserLoginRequest userLoginRequest);


}
