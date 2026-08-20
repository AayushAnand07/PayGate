package org.gateway.paygate.vault.repository;

import org.gateway.paygate.vault.entity.CardToken;
import org.gateway.paygate.vault.entity.VaultCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VaultCardRepository extends JpaRepository<VaultCard, UUID> {
}
