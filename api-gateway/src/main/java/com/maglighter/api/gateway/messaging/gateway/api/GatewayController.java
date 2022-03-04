package com.maglighter.api.gateway.messaging.gateway.api;

import com.maglighter.api.gateway.messaging.gateway.exception.ClientException;
import com.maglighter.api.gateway.messaging.gateway.service.BackendGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/rcp")
public class GatewayController {

    private final BackendGatewayService backendService;

    @GetMapping
    public ResponseEntity<MessagingResponse<Page<ResponseDTO>>> executeRcp(Pageable pageable)
        throws ClientException {
        return ResponseEntity.ok(backendService.executeRcp(pageable));
    }

}
