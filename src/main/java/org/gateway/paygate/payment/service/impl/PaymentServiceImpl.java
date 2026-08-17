package org.gateway.paygate.payment.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.enums.OrderStatus;
import org.gateway.paygate.common.enums.PaymentMethod;
import org.gateway.paygate.common.enums.PaymentStatus;
import org.gateway.paygate.common.exception.BusinessRuleViolationException;
import org.gateway.paygate.common.exception.ResourceNotFoundException;
import org.gateway.paygate.payment.dto.request.PaymentInitRequestDto;
import org.gateway.paygate.payment.dto.response.PaymentResponse;
import org.gateway.paygate.payment.entity.OrderRecord;
import org.gateway.paygate.payment.entity.Payment;
import org.gateway.paygate.payment.gateway.PaymentGatewayRouter;
import org.gateway.paygate.payment.gateway.dto.PaymentRequest;
import org.gateway.paygate.payment.gateway.dto.PaymentResult;
import org.gateway.paygate.payment.mapper.PaymentMapper;
import org.gateway.paygate.payment.repository.OrderRepository;
import org.gateway.paygate.payment.repository.PaymentRepository;
import org.gateway.paygate.payment.service.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantID, PaymentInitRequestDto request) {
        OrderRecord orderRecord = orderRepository.findByIdAndMerchantId(request.orderId(), merchantID).orElseThrow(
                ()-> new ResourceNotFoundException("Order not found ",request.orderId())
        );
        if(orderRecord.getOrderStatus()!= OrderStatus.CREATED && orderRecord.getOrderStatus()!= OrderStatus.ATTEMPTED){
            log.error("Order is not in a valid state for payment initiation. Order ID: {}, Current Status: {}", request.orderId(), orderRecord.getOrderStatus());
            throw new BusinessRuleViolationException("ORDER_NOT_PAYABLE","Order cannot accept pament in status: "+ orderRecord.getOrderStatus());

        }
        orderRecord.setOrderStatus(OrderStatus.ATTEMPTED);
        orderRecord.setAttempts(orderRecord.getAttempts()+1);
        Payment payment = Payment.builder()
                .order(orderRecord)
                .merchantId(merchantID)
                .amount(orderRecord.getAmount())
                .status(PaymentStatus.CREATED)
                .method(request.paymentMethod())
                .methodDetails(request.methodDetails())
                .build();
        payment = paymentRepository.save(payment);
        PaymentRequest paymentRequest =  new PaymentRequest(
                payment.getId(),
                orderRecord.getId(),
                merchantID,
                orderRecord.getAmount(),
                request.paymentMethod(),
                request.methodDetails()
        );
       PaymentResult result = paymentGatewayRouter.initiate(paymentRequest);
        if(result instanceof PaymentResult.Pending pending) {
            payment.setProcessorReference(pending.registerationReference());
        }else if(result instanceof PaymentResult.Failure failure){
            payment.setStatus(PaymentStatus.FAILED);
            payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDescription());
        }
        else if(result instanceof PaymentResult.Success success){

        }

        payment =paymentRepository.save(payment);
        orderRepository.save(orderRecord);
        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse capture(UUID merchantId, UUID paymentId) {
        Payment payment = paymentRepository.findByIdAndMerchantId(paymentId, merchantId).orElseThrow(
                () -> new ResourceNotFoundException("Payment not found ", paymentId)
        );

        payment.setStatus(PaymentStatus.CAPTURING);
        PaymentResult paymentResult = paymentGatewayRouter.capture(payment.getMethod(), paymentId);
        if(paymentResult instanceof  PaymentResult.Success success){
            payment.setStatus(PaymentStatus.CAPTURED);
            payment.setCapturedAt(LocalDateTime.now());
            log.info("Payment captured successfully for paymentId: {}", paymentId);
        }else if(paymentResult instanceof PaymentResult.Failure failure){
            payment.setStatus(PaymentStatus.AUTHORIZED);
            payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDescription());
            log.warn("Failed to capture payment for paymentId: {}", paymentId);
        }
        payment = paymentRepository.save(payment);
        return paymentMapper.toResponse(payment);
    }
}
