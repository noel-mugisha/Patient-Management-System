package com.demo.patientservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiErrorResponse(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        int statusCode,
        String errorReason,
        Object message
) {
    public static ApiErrorResponseBuilder builder() {
        return new ApiErrorResponseBuilder().timestamp(LocalDateTime.now());
    }
}