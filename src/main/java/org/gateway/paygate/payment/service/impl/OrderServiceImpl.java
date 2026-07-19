package org.gateway.paygate.payment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.enums.OrderStatus;
import org.gateway.paygate.common.exception.BusinessRuleViolationException;
import org.gateway.paygate.common.exception.DuplicateResourceException;
import org.gateway.paygate.common.exception.ResourceNotFoundException;
import org.gateway.paygate.payment.dto.request.CreateOrderRequest;
import org.gateway.paygate.payment.dto.response.OrderResponse;
import org.gateway.paygate.payment.dto.response.PaymentResponse;
import org.gateway.paygate.payment.entity.OrderRecord;
import org.gateway.paygate.payment.entity.Payment;
import org.gateway.paygate.payment.mapper.OrderMapper;
import org.gateway.paygate.payment.mapper.PaymentMapper;
import org.gateway.paygate.payment.repository.OrderRepository;
import org.gateway.paygate.payment.repository.PaymentRepository;
import org.gateway.paygate.payment.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultOrderExpiryMinutes;

    @Override
    @Transactional
    public OrderResponse createOrder(UUID merchantId, CreateOrderRequest request) {
        if(orderRepository.existsByMerchantIdAndReceipt(merchantId,request.reciept())){
            throw new DuplicateResourceException("ORDER_RECIEPT_DUPLICATE","Order with the same reciept already exists for this merchant");
        }
        OrderRecord order = OrderRecord.builder().receipt(request.reciept()).
                amount(request.amount()).notes(request.notes()).
                merchantId(merchantId).orderStatus(OrderStatus.CREATED).
                expiresAt(request.expiresAt()!=null? request.expiresAt() : LocalDateTime.now().
                        plusMinutes(defaultOrderExpiryMinutes)).build();
        order = orderRepository.save(order);


        // TODO: Send Kaka event

        return  orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {

        OrderRecord orderRecord = orderRepository.findByIdAndMerchantId(orderId,merchantId).orElseThrow(() -> new ResourceNotFoundException("ORDER", orderId));
        return orderMapper.toResponse(orderRecord);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID merchantId, UUID orderId) {
        OrderRecord order  = orderRepository.findByIdAndMerchantId(orderId,merchantId).orElseThrow(() -> new ResourceNotFoundException("ORDER", orderId));

        if(order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.PAID){
            throw new BusinessRuleViolationException("ORDER_CANNOT_CANCEL","Cannot cancel order with status: "+order.getOrderStatus().name());
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        return orderMapper.toResponse(order);
    }

    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {
        orderRepository.findByIdAndMerchantId(orderId, merchantId).orElseThrow(() -> new ResourceNotFoundException("ORDER", orderId));
        List<Payment> paymentList = paymentRepository.findByOrderId(orderId);
        return paymentMapper.toResponseList(paymentList);
    }
}
