package org.gateway.paygate.merchant.service;

import org.gateway.paygate.merchant.dto.request.MerchantSignupRequest;
import org.gateway.paygate.merchant.dto.response.MerchantResponse;

public interface AuthService {

    MerchantResponse signup(MerchantSignupRequest request);
}
