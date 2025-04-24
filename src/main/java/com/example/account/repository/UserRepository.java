package com.example.account.repository;

import com.example.account.domain.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    UserView queryUserByUserId(String userId);
    UserView findByUserId(@NotNull String userId);

    User readByUserId(@NotNull String userId);


    @Modifying
    @Query("update User u set u.status = :status where u.userId = :userId ")
    void updateByUserIdStatus(@Param("userId") String userId, @Param("status") String status);
}
