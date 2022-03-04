package com.maglighter.api.gateway.messaging.backend.service;

import com.maglighter.api.gateway.messaging.gateway.api.BackendServiceApi;
import com.maglighter.api.gateway.messaging.gateway.api.MessagingResponse;
import com.maglighter.api.gateway.messaging.gateway.api.ResponseDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class BackendServiceImpl implements BackendServiceApi {

    @Override
    @ServiceActivator(inputChannel = "executeRcp")
    public MessagingResponse<Page<ResponseDTO>> executeRcp(@Payload Pageable pageable) {
        return MessagingResponse.data(new PageImpl<>(
                                          List.of(
                                              ResponseDTO.builder()
                                                         .response("message from backend")
                                                         .build()
                                          )
                                      )
        );
    }
}
