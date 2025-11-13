package com.tekton.tenpo.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataCallHistoryRepository extends JpaRepository<CallHistoryEntity, Long> {}

