package org.gateway.paygate.merchant.entity;


import jakarta.persistence.*;
import lombok.*;
import org.gateway.paygate.common.entity.BaseEntity;
import org.gateway.paygate.common.enums.BusinessType;
import org.gateway.paygate.common.enums.MerchantStatus;

import java.util.UUID;

@Entity
@Table(name="merchant" , indexes = {
        @Index(name = "idx_merchant_merchantStatus", columnList = "merchantStatus"),
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Merchant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(length = 25)
    private String contactNumber;

    @Column(length = 25)
    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    private String businessName;
    private String websiteUrl;

    @Column(length = 25,nullable = false)
    private MerchantStatus merchantStatus = MerchantStatus.PENDING_KYC;
    @Column(length = 20)
    private String gstId;
    @Column(length = 20)
    private String panId;

    private String settlementBankAccountID;
    private String settlementBankifsc;
    private String getSettlementBankAccountHolderName;

}
