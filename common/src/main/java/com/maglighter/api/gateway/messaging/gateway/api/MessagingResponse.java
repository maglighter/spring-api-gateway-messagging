package com.maglighter.api.gateway.messaging.gateway.api;

import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessagingResponse<T> implements Serializable {

    private T data;
    private Status status;

    private MessagingResponse() {
    }

    public static <T> MessagingResponse<T> data(T data) {
        return new MessagingResponse<>(data, Status.OK);
    }

    public static MessagingResponse<Void> error() {
        return new MessagingResponse<>(null, Status.INTERNAL_ERROR);
    }
}
