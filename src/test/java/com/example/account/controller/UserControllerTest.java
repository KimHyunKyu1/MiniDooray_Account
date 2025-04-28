package com.example.account.controller;

import com.example.account.domain.*;
import com.example.account.exception.UserNotFoundException;
import com.example.account.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("정상적인 요청")
    void testRegisterUserSuccess() throws Exception {
        // Given
        Status status = new Status("REGISTERED");
        UserCreateCommand command = new UserCreateCommand("testUser", "password123", "test@example.com", status);
        UserView userView = new UserView() {
            @Override
            public String getUserId() {
                return "testUser";
            }

            @Override
            public String getEmail() {
                return "test@example.com";
            }

            @Override
            public Status getStatus() {
                return status;
            }
        };

        when(userService.registerUser(any(UserCreateCommand.class))).thenReturn(userView);
        String requestJson = objectMapper.writeValueAsString(command);

        // When
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.status").value("REGISTERED"));
    }

    @Test
    @DisplayName("잘못된 이메일 요청")
    void testRegisterUserInvalidEmail() throws Exception {
        // Given
        Status status = new Status("REGISTERED");
        UserCreateCommand command = new UserCreateCommand("testUser", "password123", "email", status);
        String requestJson = objectMapper.writeValueAsString(command);

        // When
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))

                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        // Given: 사용자 업데이트 요청과 모킹된 응답 준비
        Status inactiveStatus = new Status("REGISTERED");
        UserUpdateRequest updateRequest = new UserUpdateRequest("testUser", inactiveStatus);
        UserView userView = mock(UserView.class);
        when(userView.getUserId()).thenReturn("testUser");
        when(userView.getEmail()).thenReturn("test@example.com");
        when(userView.getStatus()).thenReturn(inactiveStatus);
        when(userService.updateUser(any(UserUpdateRequest.class))).thenReturn(userView);
        String requestJson = objectMapper.writeValueAsString(updateRequest);

        // When: PUT /api/users 호출
        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))

                // Then: 성공 응답 검증
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testUser"))
                .andExpect(jsonPath("$.status.status").value("INACTIVE"));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        // Given: 존재하지 않는 사용자에 대한 업데이트 요청
        Status inactiveStatus = new Status("REGISTERED");
        UserUpdateRequest updateRequest = new UserUpdateRequest("nonExistentUser", inactiveStatus);
        when(userService.updateUser(any(UserUpdateRequest.class)))
                .thenThrow(new UserNotFoundException("User not found"));
        String requestJson = objectMapper.writeValueAsString(updateRequest);

        // When: PUT /api/users 호출
        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))

                // Then: 404 Not Found 검증
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUserInvalidStatus() throws Exception {
        // Given: 유효하지 않은 상태로 업데이트 요청
        Status invalidStatus = new Status("REGISTERED");
        UserUpdateRequest updateRequest = new UserUpdateRequest("testUser", invalidStatus);
        when(userService.updateUser(any(UserUpdateRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid status value: INVALID"));
        String requestJson = objectMapper.writeValueAsString(updateRequest);

        // When: PUT /api/users 호출
        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))

                // Then: 400 Bad Request 검증
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        // Given: 사용자 삭제 요청
        Status status = new Status("REGISTERED");
        UserCreateCommand command = new UserCreateCommand("testUser", "password123", "test@example.com", status);
        doNothing().when(userService).deleteUser("testUser");
        String requestJson = objectMapper.writeValueAsString(command);

        // When: DELETE /api/users 호출
        mockMvc.perform(delete("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))

                // Then: 성공 응답 검증
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        // Given: 존재하지 않는 사용자 삭제 요청
        Status status = new Status("REGISTERED");
        UserCreateCommand command = new UserCreateCommand("nonExistentUser", "password123", "test@example.com", status);
        doThrow(new UserNotFoundException("User not found")).when(userService).deleteUser("nonExistentUser");
        String requestJson = objectMapper.writeValueAsString(command);

        // When: DELETE /api/users 호출
        mockMvc.perform(delete("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))

                // Then: 404 Not Found 검증
                .andExpect(status().isNotFound());
    }
}