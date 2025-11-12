package com.tekton.tenpo.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tekton.tenpo.domain.model.CallHistory;

@Repository
public interface SpringDataCallHistoryRepository extends JpaRepository<CallHistory, Long> {}

