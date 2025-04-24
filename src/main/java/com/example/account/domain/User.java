package com.example.account.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.EntityGraph;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "users")
public class User {
    public User(String userId, String password, String email, Status status){
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.status =  status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Setter
    @Column(unique = true)
    private String userId;

    @NotNull
    @Setter
    private String password;

    @NotNull
    @Setter
    @Email
    private String email;

    @ManyToOne
    @NotNull
    @Setter
    @JoinColumn(name = "status")
    private Status status;

}
