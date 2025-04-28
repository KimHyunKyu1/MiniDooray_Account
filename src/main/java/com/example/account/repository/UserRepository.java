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

    @Query("select u from User u where u.userId = :userId")
    UserView queryUserByUserId(@Param("userId") String userId);

    @Query("select u from User u where u.userId = :userId")
    UserView findByUserId(@NotNull @Param("userId") String userId);

    User readByUserId(@NotNull String userId);

    @Modifying
    @Transactional
    @Query("update User u set u.status = (select s from Status s where s.status = :status) where u.userId = :userId")
    void updateByUserIdStatus(@Param("userId") String userId, @Param("status") String status);

    @Query("SELECT u FROM User u WHERE u.userId = :#{#request.userId} AND u.password = :#{#request.password}")
    User matchUserLoginRequest_UserId_Password(@Param("request") UserLoginRequest userLoginRequest);




}
