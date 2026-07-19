package org.gateway.paygate.merchant.entity;

import jakarta.persistence.*;
import org.gateway.paygate.common.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers" , indexes = {
        @Index(name = "idx_customers_merchant_id", columnList = "merchant_id"),
        @Index(name = "idx_customers_email", columnList = "email"),

})
public class Customers extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "merchant_id",nullable = false)
    private Merchant merchant;

    @Column(length = 200)
    private String name;

    @Column(length = 200)
    private String email;

    @Column(length = 20)
    private String contactNumber;

    private LocalDateTime deletedAt;
}
