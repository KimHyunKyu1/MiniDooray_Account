package com.example.account.service;

import com.example.account.domain.*;
import com.example.account.exception.UserNotFoundException;
import com.example.account.repository.StatusRepository;
import com.example.account.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserCreateCommand userCreateCommand;
    private UserUpdateRequest userUpdateRequest;
    private UserLoginRequest userLoginRequest;
    private User user;
    private UserView userView;
    private Status status;

    @BeforeEach
    void setUp() {
        status = new Status("REGISTERED");
        userCreateCommand = new UserCreateCommand("user11", "password123", "user11@example.com", status);
        userUpdateRequest = new UserUpdateRequest("user11", status);
        userLoginRequest = new UserLoginRequest("user11", "password");
        user = new User("user11", "encodedPassword", "user11@example.com", status);
        userView = new UserView() {
            @Override
            public String getUserId() {
                return "user11";
            }
            @Override
            public String getEmail() {
                return "user11@example.com";
            }
            @Override
            public Status getStatus() {
                return status;
            }
        };
    }

    @Test
    @DisplayName("유저 등록 성공")
    void testRegisterUserSuccess() {
        // Given
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.queryUserByUserId("user11")).thenReturn(userView);

        // When
        UserView result = userService.registerUser(userCreateCommand);

        // Then
        assertNotNull(result);
        assertEquals("user11", result.getUserId());
        assertEquals("user11@example.com", result.getEmail());
        assertEquals("REGISTERED", result.getStatus().getStatus());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(userRepository).queryUserByUserId("user11");
    }


    @Test
    @DisplayName("유저 등록 실패 - 예외 발생")
    void testRegisterUserFailure() {
        // Given
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.registerUser(userCreateCommand));
        verify(userRepository).save(any(User.class));
        verify(userRepository, never()).queryUserByUserId(anyString());
    }

    @Test
    @DisplayName("유저 삭제 성공")
    void testDeleteUserSuccess() {
        // Given
        when(userRepository.readByUserId("user11")).thenReturn(user);

        // When
        userService.deleteUser("user11");

        // Then
        verify(userRepository).readByUserId("user11");
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("유저 삭제 실패 - 유저 없음")
    void testDeleteUserNotFound() {
        // Given
        when(userRepository.readByUserId("user11")).thenReturn(null);

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser("user11"));
        verify(userRepository).readByUserId("user11");
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    @DisplayName("유저 업데이트 성공")
    void testUpdateUserSuccess() {
        // Given
        when(statusRepository.findAllByStatus("REGISTERED")).thenReturn(List.of(status));
        when(userRepository.findByUserId("user11")).thenReturn(userView);

        // When
        UserView result = userService.updateUser(userUpdateRequest);

        // Then
        assertNotNull(result);
        assertEquals("user11", result.getUserId());
        assertEquals("REGISTERED", result.getStatus().getStatus());
        verify(statusRepository).findAllByStatus("REGISTERED");
        verify(userRepository).updateByUserIdStatus("user11", "REGISTERED");
        verify(userRepository).findByUserId("user11");
    }

    @Test
    @DisplayName("유저 업데이트 실패 - 유효하지 않은 상태")
    void testUpdateUserInvalidStatus() {
        // Given
        when(statusRepository.findAllByStatus("INVALID")).thenReturn(Collections.emptyList());
        userUpdateRequest = new UserUpdateRequest("user11", new Status("INVALID"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userUpdateRequest));
        verify(statusRepository).findAllByStatus("INVALID");
        verify(userRepository, never()).updateByUserIdStatus(anyString(), anyString());
    }

    @Test
    @DisplayName("유저 로그인 성공")
    void testMatchUserLoginRequestSuccess() {
        // Given


        when(userRepository.GetUserByUserId("user11")).thenReturn(user);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(userRepository.findByUserId("user11")).thenReturn(userView);

        // When
        UserView result = userService.matchUserLoginRequest(userLoginRequest);
        // Then
        assertNotNull(result);
        assertEquals("user11", result.getUserId());
        assertEquals("user11@example.com", result.getEmail());
        assertEquals("REGISTERED", result.getStatus().getStatus());
        verify(userRepository).GetUserByUserId("user11");
        verify(passwordEncoder).matches("password", "encodedPassword");
        verify(userRepository).findByUserId("user11");
    }

    @Test
    @DisplayName("유저 로그인 실패 - 사용자 없음")
    void testMatchUserLoginRequestFailure_UserNotFound() {
        // Given
        when(userRepository.GetUserByUserId("user11")).thenReturn(null);

        // When & Then
        assertThrows(NullPointerException.class, () -> userService.matchUserLoginRequest(userLoginRequest));
        verify(userRepository).GetUserByUserId("user11");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(userRepository, never()).findByUserId("user11");
    }


}

