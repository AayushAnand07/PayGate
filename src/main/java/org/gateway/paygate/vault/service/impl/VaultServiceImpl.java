package org.gateway.paygate.vault.service.impl;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.entity.Money;
import org.gateway.paygate.common.enums.CardBrand;
import org.gateway.paygate.common.exception.ResourceNotFoundException;
import org.gateway.paygate.common.util.RandomizerUtil;
import org.gateway.paygate.payment.processor.PaymentProcessorRouter;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorRequest;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorResponse;
import org.gateway.paygate.vault.config.VaultEncryptionConfig;
import org.gateway.paygate.vault.dto.request.TokenizeRequest;
import org.gateway.paygate.vault.dto.response.TokenizeResponse;
import org.gateway.paygate.vault.entity.CardToken;
import org.gateway.paygate.vault.entity.VaultCard;
import org.gateway.paygate.vault.repository.CardTokenRepository;
import org.gateway.paygate.vault.repository.VaultCardRepository;
import org.gateway.paygate.vault.service.VaultService;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final CardTokenRepository cardTokenRepository;
    private final VaultCardRepository vaultCardRepository;
    private final BytesEncryptor dekEncryptor;
    private PaymentProcessorRouter paymentProcessorRouter;

    @Override
    @Transactional
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {
        String lastFour =request.pan().substring(request.pan().length() - 4);
        String bin = request.pan().substring(0, 6);
        CardBrand cardBrand = detectBrand(request.pan());

        byte[] dek = KeyGenerators.secureRandom(32).generateKey();
        byte[] encryptedPan = VaultEncryptionConfig.panEncryptor(dek).encrypt(request.pan().getBytes(StandardCharsets.UTF_8));

        byte[] encryptedDek = dekEncryptor.encrypt(dek);

        VaultCard vaultCard = VaultCard.builder()
                .lastFour(lastFour)
                .bin(bin)
                .encryptedPan(encryptedPan)
                .encryptedDek(encryptedDek)
                .brand(cardBrand.name())
                .expiryMonth(String.format("%02d", request.expiryMonth()))
                .expiryYear(String.valueOf(request.expiryYear()))
                .cardHolderName(request.cardHolderName())
                .build();
        String token = "tok_ "+ RandomizerUtil.randomBase64(32);
        cardTokenRepository.save(CardToken.builder().
                token(token).
                vaultCard(vaultCard).
                customer(request.customerId()).
                merchant(merchantId).
                build());

        vaultCardRepository.save(vaultCard);
        return new TokenizeResponse(token, lastFour,cardBrand, request.expiryMonth(), request.expiryYear());
    }

    @Override
    public PaymentProcessorResponse charge(UUID paymentId,String token, Money amount, Map<String, Object> methodDetails) {
        CardToken cardToken = cardTokenRepository.findByTokenAndRevokedAtIsNull(token).orElseThrow(() -> new ResourceNotFoundException("Card Token",token));
        VaultCard vaultCard = cardToken.getVaultCard();
        byte[] bytePan = null;
        try {
            byte[] decryptedDek = dekEncryptor.decrypt(vaultCard.getEncryptedDek());
            bytePan = VaultEncryptionConfig.panEncryptor(decryptedDek).decrypt(vaultCard.getEncryptedPan());
            String pan = new String(bytePan, StandardCharsets.UTF_8);
            String expiry = vaultCard.getExpiryMonth() + "/" + vaultCard.getExpiryYear();

            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.card(
                    paymentId,
                    amount,
                    pan,
                    expiry,
                    methodDetails);

            PaymentProcessorResponse paymentProcessorResponse = paymentProcessorRouter.charge(paymentProcessorRequest);
            log.info("Vault Charged registered, token={}*****", token.substring(0, 4));


            return paymentProcessorResponse;
        }
        catch (Exception e){
            log.warn("Vault Charged failed, token={}*****", token.substring(0, 4));
            return new PaymentProcessorResponse.Failure("VAULT_CHARGE_FAILED", "An internal error occurred while processing the charge");
        }finally {
            if(bytePan!=null){
                Arrays.fill(bytePan, (byte) 0);
            }
        }
    }

    private CardBrand detectBrand(String pan) {
        if(pan.startsWith("4")) {
            return CardBrand.VISA;
        } else if(pan.startsWith("5")) {
            return CardBrand.MASTERCARD;
        } else if(pan.startsWith("3")) {
            return CardBrand.AMEX;
        } else if(pan.startsWith("6")) {
            return CardBrand.DISCOVER;
        } else {
            return CardBrand.RUPAY;
        }
    }
}
