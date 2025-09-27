package com.demo.patientservice.dto.response;

import lombok.Builder;

@Builder
public record PatientResponseDto (
        String id,
        String name,
        String email,
        String address,
        String dateOfBirth
) {}
