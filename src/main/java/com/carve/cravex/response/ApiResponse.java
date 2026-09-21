package com.carve.cravex.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private String serviceName;
    private boolean success;
    private String type;
    private T payload;
    private int statusCode;
    private LocalDateTime timeStamp;

    public static <T> ApiResponse<T> success(HttpStatus httpStatus,T payload){
        return ApiResponse.<T>builder()
                .serviceName("CraveX")
                .success(true)
                .type(payload!=null?payload.getClass().getSimpleName():"void")
                .statusCode(httpStatus.value())
                .timeStamp(LocalDateTime.now())
                .payload(payload)
                .build();
    }

    public static <T> ApiResponse<T> failed(HttpStatus httpStatus, T payload){
        return ApiResponse.<T>builder()
                .serviceName("AUTH-SERVICE")
                .success(false)
                .type(payload!=null?payload.getClass().getSimpleName():"void")
                .statusCode(httpStatus.value())
                .payload(payload)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
