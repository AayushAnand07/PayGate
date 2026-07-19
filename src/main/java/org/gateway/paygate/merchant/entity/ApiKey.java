package org.gateway.paygate.merchant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.gateway.paygate.common.entity.BaseEntity;
import org.gateway.paygate.common.enums.Environment;

import java.util.UUID;

@Entity
@Table(name = "api_key" ,
        indexes = {
        @Index(name = "idx_api_key_merchant_env", columnList = "merchant_id, environment , enabled")

        })
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiKey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "merchant_id",nullable = false)
    private Merchant merchant;

    @Column(unique = true, nullable = false,length = 50)
    private String keyId;

    @Column(nullable = false,length = 200)
    private String keySecretHash;

    @Column(length = 200)
    private String previousKeySecretHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 10)
    private Environment environment;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    private java.time.LocalDateTime lastUsedAt;
    private java.time.LocalDateTime rotatedAt;
    private java.time.LocalDateTime gracePeriodExpiresAt;



}
