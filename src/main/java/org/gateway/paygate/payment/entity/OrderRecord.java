package org.gateway.paygate.payment.entity;


import jakarta.persistence.*;
import org.gateway.paygate.common.entity.Money;
import org.gateway.paygate.common.enums.OrderStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_record")
public class OrderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "merchant_id", nullable = false)
    private UUID merchant_id;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrderStatus orderStatus = OrderStatus.CREATED;

    private Integer attempts;

    @JdbcTypeCode((SqlTypes.JSON))
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> notes;

    @Column(name = "expires_at" , nullable = false)
    private LocalDateTime expiresAt;


}
