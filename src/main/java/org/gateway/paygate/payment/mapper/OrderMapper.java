package org.gateway.paygate.payment.mapper;

import org.gateway.paygate.payment.dto.response.OrderResponse;
import org.gateway.paygate.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toResponse(OrderRecord orderRecord);
}
