package org.gateway.paygate.merchant.service.impl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.exception.ResourceNotFoundException;
import org.gateway.paygate.merchant.entity.Customers;
import org.gateway.paygate.merchant.entity.Merchant;
import org.gateway.paygate.merchant.repository.CustomerRepository;
import org.gateway.paygate.merchant.repository.MerchantRepository;
import org.gateway.paygate.merchant.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final MerchantRepository merchantRepository;

    @Transactional
    @Override
    public UUID findOrCreate(UUID merchantId, String email, String name, String contactNumber) {

        if(email == null || email.isBlank()) {
            return null;
        }
      return customerRepository.findByMerchant_IdAndEmail(merchantId, email).map(Customers:: getId).orElseGet(() -> createNew(merchantId, email, name, contactNumber));
    }

    private UUID createNew(UUID merchantId, String email, String name, String phone) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", merchantId));

        Customers customer = Customers.builder()
                .merchant(merchant)
                .email(email)
                .name(name)
                .contactNumber(phone)
                .build();

        customer = customerRepository.save(customer);
        log.info("Customer created via findOrCreate id={} merchantId={} email={}",
                customer.getId(), merchantId, email);
        return customer.getId();
    }
}

