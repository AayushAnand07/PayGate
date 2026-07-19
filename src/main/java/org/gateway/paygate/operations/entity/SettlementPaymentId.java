package org.gateway.paygate.operations.entity;

import jakarta.persistence.Embeddable;
import org.gateway.paygate.common.entity.BaseEntity;

import java.util.UUID;

@Embeddable
public class SettlementPaymentId extends BaseEntity {

    private UUID settlementId;

    private UUID paymentId;
}

