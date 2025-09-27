package com.demo.patientservice.dto.request;

import com.demo.patientservice.validation.LowerCase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record PatientRequestDto(
        @NotNull(message = "Name field is required")
        String name,
        @NotNull(message = "Email field is required")
        @Email(message = "Enter a valid email address")
        @LowerCase(message = "Email field must be lowercase")
        String email,
        @NotNull(message = "Address field is required")
        String address,
        @NotNull(message = "Date of birth field is required")
        String dateOfBirth
) {
}
