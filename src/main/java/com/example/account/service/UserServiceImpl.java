package com.example.account.service;

import com.example.account.domain.*;
import com.example.account.exception.UserNotFoundException;
import com.example.account.repository.StatusRepository;
import com.example.account.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final StatusRepository statusRepository;



    @Override
    @Transactional
    public UserView registerUser(UserCreateCommand command) {

        try {

            User user = new User(command.getUserId(),
                    command.getPassword(),
                    command.getEmail(),
                    command.getStatus());
            System.out.println(user.getUserId());
            userRepository.save(user);

            userRepository.flush();
            System.out.println(user.getUserId());
            UserView userView = userRepository.queryUserByUserId(command.getUserId());
            return userView;
        } catch (Exception e) {
            return null;
        }

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
        // 유저의 status 정의
        String statusStr = userUpdateRequest.status().getStatus();
        // Status가 존재하는 지 검증 -삭제가능
        if (statusRepository.findAllByStatus(statusStr).isEmpty()) {
            throw new IllegalArgumentException("Invalid status value: " + statusStr);
        }
        userRepository.updateByUserIdStatus(userUpdateRequest.userId(), statusStr);
        return userRepository.findByUserId(userUpdateRequest.userId());
    }
}

//    @Override
//    public UserLoginRequest matchUserLoginRequest(UserLoginRequest userLoginRequest) {
//        Optional<UserLoginRequest> user = userRepository.matchUserLoginRequest_UserId_Password(userLoginRequest.getUserId(), userLoginRequest.getPassword());
//        if (user.isEmpty()) {
//            throw new UserNotFoundException("User not found");
//        }
//        return user.orElse(null);
//    }
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
