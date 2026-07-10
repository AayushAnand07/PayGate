package org.gateway.paygate.operations.entity;


import jakarta.persistence.*;
import org.gateway.paygate.common.enums.WebhookEventStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.SQLType;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "webhook_event")
public class WebhookEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @Column(nullable = false , length = 100)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object>  payload;

    @Column(nullable = false)
    private String targetUrl;

    @Column(nullable = false)
    private String signature;

    @Enumerated(EnumType.STRING)
    @Column(name = "status" , nullable = false)
    private WebhookEventStatus status;


    private Integer attempts = 0;

    private LocalDateTime  maxRetryAt;

    private LocalDateTime  lastAttemptedAt;

    @Column(length = 1000)
    private Integer lastResponseCode;

    private String lastResponseBody;

    private LocalDateTime deliveredAt;























}
