package com.maglighter.api.gateway.messaging.gateway.service;

import com.maglighter.api.gateway.messaging.gateway.api.MessagingResponse;
import com.maglighter.api.gateway.messaging.gateway.api.ResponseDTO;
import com.maglighter.api.gateway.messaging.gateway.messaging.BackendServiceGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BackendGatewayServiceImpl implements BackendGatewayService {

    private final BackendServiceGateway backendServiceGateway;

    @Override
    public MessagingResponse<Page<ResponseDTO>> executeRcp(Pageable pageable) {
        return backendServiceGateway.executeRcp(pageable);
    }
}
