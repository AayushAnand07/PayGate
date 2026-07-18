package org.gateway.paygate.merchant.repository;

import org.gateway.paygate.merchant.dto.response.ApiKeyResponse;
import org.gateway.paygate.merchant.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApiKeyRepository  extends JpaRepository<ApiKey, UUID> {

    List<ApiKey> findByMerchant_Id(UUID merchantId);
}
