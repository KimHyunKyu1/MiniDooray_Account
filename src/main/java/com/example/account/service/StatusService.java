package com.example.account.service;

import com.example.account.domain.Status;
import com.example.account.repository.StatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StatusService {
    private StatusRepository statusRepository;
    public Optional<Status> getStatus(String status) {
        return statusRepository.findById(status);
    }
    public List<Status> findAll() {
        return statusRepository.findAll();
    }

}
