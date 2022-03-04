package com.maglighter.api.gateway.messaging.gateway.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BackendServiceApi {

    MessagingResponse<Page<ResponseDTO>> executeRcp(Pageable pageable);
}
