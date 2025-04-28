package com.example.account.service;

import com.example.account.domain.*;
import com.example.account.exception.UserNotFoundException;
import com.example.account.repository.StatusRepository;
import com.example.account.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserView registerUser(UserCreateCommand command) {

        String rawPw = command.getPassword();
        String encodedPw = passwordEncoder.encode(rawPw);

        User user = new User(
                command.getUserId(),
                encodedPw,
                command.getEmail(),
                command.getStatus()
        );
        userRepository.save(user);

        return userRepository.queryUserByUserId(user.getUserId());
    }


    @Override
    public void deleteUser(String userId) {
        User user = userRepository.readByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public UserView updateUser(UserUpdateRequest userUpdateRequest) {
        String statusStr = userUpdateRequest.status().getStatus();
        if (statusRepository.findAllByStatus(statusStr).isEmpty()) {
            throw new IllegalArgumentException("Invalid status value: " + statusStr);
        }
        userRepository.updateByUserIdStatus(userUpdateRequest.userId(), statusStr);
        return userRepository.findByUserId(userUpdateRequest.userId());
    }

    @Override
    public UserView matchUserLoginRequest(UserLoginRequest loginReq) {
        User user = userRepository.GetUserByUserId(loginReq.getUserId());


        if (!passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("password incorrect");
        }

        return userRepository.findByUserId(loginReq.getUserId());
    }

}


//
//    @Override
//    public UserCreateCommand matchUserCreateCommand(UserCreateCommand userCreateCommand) {
//        User user = new User(userCreateCommand.getUserId(), userCreateCommand.getPassword(), userCreateCommand.getEmail(), userCreateCommand.getStatus());
//        try{
//            userRepository.save(user);
//        } catch (Exception e) {
//            throw new UserNotFoundException("User not found");
//        }
//        return userCreateCommand;
//    }
//}
