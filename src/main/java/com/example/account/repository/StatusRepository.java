package com.example.account.repository;

import com.example.account.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, String> {
    List<Status> findAllByStatus(String status);
    Optional<Status> findByStatus(String status);
}
