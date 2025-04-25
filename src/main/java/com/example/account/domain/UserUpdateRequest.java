package com.example.account.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public record UserUpdateRequest(String userId, Status status) {}
