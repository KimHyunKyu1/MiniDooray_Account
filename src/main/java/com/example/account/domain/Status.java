package com.example.account.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Status {

    @Getter
    @Setter
    @Id
    private String status;

    public Status(String registered) {
        this.status = registered;
    }
}
