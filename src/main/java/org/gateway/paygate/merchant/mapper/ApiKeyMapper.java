package org.gateway.paygate.merchant.mapper;

import org.gateway.paygate.merchant.dto.response.ApiKeyCreateResponse;
import org.gateway.paygate.merchant.dto.response.ApiKeyResponse;
import org.gateway.paygate.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    ApiKeyCreateResponse toApiKeyCreateResponse(ApiKey apiKey);
    List<ApiKeyResponse> toApiKeyCreateResponseList(List<ApiKey> apiKeys);

}
