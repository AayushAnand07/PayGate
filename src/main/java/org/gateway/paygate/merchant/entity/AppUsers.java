package org.gateway.paygate.merchant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.gateway.paygate.common.entity.BaseEntity;
import org.gateway.paygate.common.enums.UserRole;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
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
public class AppUsers extends BaseEntity implements UserDetails {


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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public @Nullable String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
