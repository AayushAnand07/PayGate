package org.gateway.paygate.merchant.service;


import java.util.UUID;

public interface CustomerService {

    UUID findOrCreate(UUID merchantId, String email, String name, String contactNumber);

}
