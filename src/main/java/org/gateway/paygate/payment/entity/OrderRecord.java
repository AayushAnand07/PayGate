package org.gateway.paygate.payment.entity;


import jakarta.persistence.*;
import lombok.*;
import org.gateway.paygate.common.entity.BaseEntity;
import org.gateway.paygate.common.entity.Money;
import org.gateway.paygate.common.enums.OrderStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "order_record",indexes = {
        @Index(name = "idx_order_id_merchant_id", columnList = "id, merchant_id"),
        @Index(name = "idx_order_merchant_id", columnList = "merchant_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrderStatus orderStatus = OrderStatus.CREATED;

    private Integer attempts;

    @Column(length = 100)
    private String receipt;

    @JdbcTypeCode((SqlTypes.JSON))
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> notes;

    @Column(name = "expires_at" , nullable = false)
    private LocalDateTime expiresAt;


}
