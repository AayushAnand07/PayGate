package org.gateway.paygate.payment.repository;

import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.gateway.paygate.payment.dto.response.OrderResponse;
import org.gateway.paygate.payment.entity.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {


    boolean existsByMerchantIdAndReceipt(UUID merchantId, @Size(max = 100) String receipt);


    Optional<OrderRecord> findByIdAndMerchantId(UUID orderId, UUID merchantId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from OrderRecord o where o.id = :uuid and o.merchantId = :merchantID")
    Optional<OrderRecord> findByIdAndMerchantIdForUpdate(@NotNull(message = "Order ID is required") UUID uuid, UUID merchantID);
}
