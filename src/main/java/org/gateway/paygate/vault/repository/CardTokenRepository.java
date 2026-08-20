package org.gateway.paygate.vault.repository;

import org.gateway.paygate.vault.entity.CardToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CardTokenRepository  extends JpaRepository<CardToken, UUID> {
    Optional<CardToken>  findByTokenAndRevokedAtIsNull(String token);
}
