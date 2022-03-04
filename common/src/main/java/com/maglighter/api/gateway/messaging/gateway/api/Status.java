package com.maglighter.api.gateway.messaging.gateway.api;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status implements Serializable {
    OK("OK"),
    INTERNAL_ERROR("Some internal error");

    private final String description;
}
