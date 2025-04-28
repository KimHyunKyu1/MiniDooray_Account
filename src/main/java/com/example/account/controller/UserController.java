package com.example.account.controller;

import com.example.account.domain.UserCreateCommand;
import com.example.account.domain.UserLoginRequest;
import com.example.account.domain.UserUpdateRequest;
import com.example.account.domain.UserView;
import com.example.account.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping ("/api/users")
public class UserController {

    private final UserServiceImpl userServiceimpl;

//
//    @GetMapping
//    public UserCreateCommand matchUserCreateCommand(@RequestBody UserCreateCommand userCreateCommand) {
//        return userServiceimpl.matchUserCreateCommand(userCreateCommand);
//    }

    @PostMapping
    public UserView registerUserCreateCommand(@Valid @RequestBody UserCreateCommand userCreateCommand) {
        return userServiceimpl.registerUser(userCreateCommand);
    }

    @PutMapping
    public UserView updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        return userServiceimpl.updateUser(userUpdateRequest);
    }

    @DeleteMapping
    public void deleteUser(@RequestBody UserCreateCommand userCreateCommand) {
        userServiceimpl.deleteUser(userCreateCommand.getUserId());
    }

    @PostMapping("/login")
    public UserLoginRequest loginRequestUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return userServiceimpl.matchUserLoginRequest(userLoginRequest);
    }

}
