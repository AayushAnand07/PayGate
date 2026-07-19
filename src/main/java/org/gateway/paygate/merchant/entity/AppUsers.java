package org.gateway.paygate.merchant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.gateway.paygate.common.entity.BaseEntity;
import org.gateway.paygate.common.enums.UserRole;

import java.util.UUID;

@Entity
@Table(name = "app_users" ,
        indexes = {
        @Index(name = "idx_app_users_merchant_id", columnList = "merchant_id")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUsers extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true , nullable = false)
    private  String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;


}
