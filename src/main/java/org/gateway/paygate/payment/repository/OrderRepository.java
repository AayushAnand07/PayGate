package org.gateway.paygate.payment.repository;

import jakarta.validation.constraints.Size;
import org.gateway.paygate.payment.dto.response.OrderResponse;
import org.gateway.paygate.payment.entity.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {


    boolean existsByMerchantIdAndReceipt(UUID merchantId, @Size(max = 100) String receipt);

    Optional<OrderRecord> findByIdAndMerchantId(UUID orderId, UUID merchantId);
}
