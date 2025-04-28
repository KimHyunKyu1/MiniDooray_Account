package com.example.account;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class AccountApplicationTests {

    @Test
    void contextLoads() {
        assertThatCode(() -> {
        }).doesNotThrowAnyException();
    }


}
