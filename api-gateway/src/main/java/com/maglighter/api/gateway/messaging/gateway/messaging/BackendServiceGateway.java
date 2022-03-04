package com.maglighter.api.gateway.messaging.gateway.messaging;

import com.maglighter.api.gateway.messaging.gateway.api.BackendServiceApi;
import com.maglighter.api.gateway.messaging.gateway.api.MessagingResponse;
import com.maglighter.api.gateway.messaging.gateway.api.ResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultHeaders = @GatewayHeader(name = "calledMethod", expression = "#gatewayMethod.name"),
        defaultRequestChannel = "inputChannel")
public interface BackendServiceGateway extends BackendServiceApi {

    @Override
    @Gateway
    MessagingResponse<Page<ResponseDTO>> executeRcp(Pageable pageable);
}
