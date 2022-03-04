package com.maglighter.api.gateway.messaging.gateway.service;

import com.maglighter.api.gateway.messaging.gateway.api.MessagingResponse;
import com.maglighter.api.gateway.messaging.gateway.api.ResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BackendGatewayService {
    MessagingResponse<Page<ResponseDTO>> executeRcp(Pageable pageable);
}
