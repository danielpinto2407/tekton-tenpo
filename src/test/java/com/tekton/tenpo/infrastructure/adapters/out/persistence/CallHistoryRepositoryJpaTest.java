package com.tekton.tenpo.infrastructure.adapters.out.persistence;

import com.tekton.tenpo.domain.model.CallHistory;
import com.tekton.tenpo.infrastructure.adapters.out.persistence.mapper.CallHistoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CallHistoryRepositoryJpaTest {

    @Mock
    private SpringDataCallHistoryRepository repo;

    @Mock
    private CallHistoryMapper mapper;

    @InjectMocks
    private CallHistoryRepositoryJpa repositoryJpa;

    private CallHistory callHistory;
    private CallHistoryEntity callHistoryEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        callHistory = new CallHistory(
                1L,
                Instant.now(),
                "/api/test",
                "{}",
                "ok",
                200
        );

        callHistoryEntity = new CallHistoryEntity();
        callHistoryEntity.setId(callHistory.id());
        callHistoryEntity.setTimestamp(callHistory.timestamp());
        callHistoryEntity.setEndpoint(callHistory.endpoint());
        callHistoryEntity.setParameters(callHistory.parameters());
        callHistoryEntity.setResponse(callHistory.response());
        callHistoryEntity.setStatusCode(callHistory.statusCode());
    }

    @Test
    void save_shouldCallRepoWithMappedEntity() {
        when(mapper.toEntity(callHistory)).thenReturn(callHistoryEntity);

        repositoryJpa.save(callHistory);

        verify(mapper, times(1)).toEntity(callHistory);
        verify(repo, times(1)).save(callHistoryEntity);
    }

    @Test
    void findAll_shouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CallHistoryEntity> entityPage = new PageImpl<>(List.of(callHistoryEntity), pageable, 1);

        when(repo.findAll(pageable)).thenReturn(entityPage);
        when(mapper.toDomain(callHistoryEntity)).thenReturn(callHistory);

        Page<CallHistory> result = repositoryJpa.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(callHistory.endpoint(), result.getContent().get(0).endpoint());

        verify(repo, times(1)).findAll(pageable);
        verify(mapper, times(1)).toDomain(callHistoryEntity);
    }
}
