package com.example.account.service;

import com.example.account.domain.Status;
import com.example.account.repository.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatusServiceTest {

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private StatusService statusService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Status 목록 조회")
    void findAll() {
        //Given
        Status status1 = new Status("REGISTERED");
        Status status2 = new Status("DORMANT");
        Status status3 = new Status("WITHDRAWN");
        when(statusRepository.findAll()).thenReturn(List.of(status1, status2, status3));

        //When
        List<Status> statusList = statusService.findAll();


        //Then
        assertEquals(3, statusList.size());
        assertEquals("REGISTERED", statusList.get(0).getStatus());
        assertEquals("DORMANT", statusList.get(1).getStatus());
        assertEquals("WITHDRAWN", statusList.get(2).getStatus());
    }

}
