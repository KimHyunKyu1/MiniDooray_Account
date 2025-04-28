package com.example.account.service;

import com.example.account.domain.*;
import com.example.account.exception.UserNotFoundException;
import com.example.account.repository.StatusRepository;
import com.example.account.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
            userRepository.save(user);

            userRepository.flush();
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
        // Status 존재하는 지 검증 -삭제가능
        if (statusRepository.findAllByStatus(statusStr).isEmpty()) {
            throw new IllegalArgumentException("Invalid status value: " + statusStr);
        }
        userRepository.updateByUserIdStatus(userUpdateRequest.userId(), statusStr);
        return userRepository.findByUserId(userUpdateRequest.userId());
    }

    @Override
    public UserView matchUserLoginRequest(UserLoginRequest userLoginRequest) {
        User user = userRepository.matchUserLoginRequest_UserId_Password(userLoginRequest);
        UserView userView = userRepository.queryUserByUserId(userLoginRequest.getUserId());
        if (user != null) {
            return userView;
        }
        return null;
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
