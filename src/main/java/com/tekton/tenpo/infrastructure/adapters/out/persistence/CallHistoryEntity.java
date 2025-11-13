package com.tekton.tenpo.infrastructure.adapters.out.persistence;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "call_history", indexes = {
    @Index(name = "idx_timestamp", columnList = "timestamp"),
    @Index(name = "idx_endpoint", columnList = "endpoint")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false, length = 500)
    private String endpoint;

    @Column(columnDefinition = "text")
    private String parameters;

    @Column(columnDefinition = "text")
    private String response;

    @Column(nullable = false)
    private Integer statusCode;
}
