package org.gateway.paygate.merchant.service;

import jakarta.validation.Valid;
import org.gateway.paygate.merchant.dto.request.LoginRequest;
import org.gateway.paygate.merchant.dto.request.MerchantSignupRequest;
import org.gateway.paygate.merchant.dto.response.LoginResponse;
import org.gateway.paygate.merchant.dto.response.MerchantResponse;

public interface AuthService {

    MerchantResponse signup(MerchantSignupRequest request);

    LoginResponse login(@Valid LoginRequest request);
}
