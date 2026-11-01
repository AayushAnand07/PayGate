package org.gateway.paygate.merchant.repository;

import org.gateway.paygate.merchant.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customers, UUID> {
    Optional<Customers>  findByMerchant_IdAndEmail(UUID merchantId, String email);
}
