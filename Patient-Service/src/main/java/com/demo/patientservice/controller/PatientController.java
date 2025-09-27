package com.demo.patientservice.controller;

import com.demo.patientservice.dto.request.PatientRequestDto;
import com.demo.patientservice.dto.response.PatientResponseDto;
import com.demo.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Management", description = "APIs for managing patients")
public class PatientController {
    private final PatientService patientService;

    @Operation(summary = "Create a new patient")
    @PostMapping()
    public ResponseEntity<PatientResponseDto> addPatient(
            @Valid @RequestBody PatientRequestDto request,
            UriComponentsBuilder uriBuilder
    ) {
        var patientDto = patientService.savePatient(request);
        var uri = uriBuilder.path("/api/v1/patients/{id}").buildAndExpand(patientDto.id())
                .toUri();
        return ResponseEntity.created(uri).body(patientDto);
    }

    @Operation(summary = "Get all patients")
    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        var patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @Operation(summary = "Get patient by ID")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getSinglePatient(
            @Parameter(description = "ID of the patient to be retrieved", required = true)
            @PathVariable UUID id
    ) {
        var patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @Operation(summary = "Update a patient")
    @PutMapping(value = "/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @Parameter(description = "ID of the patient to be updated", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody PatientRequestDto request
    ) {
        var updatedPatient = patientService.updatePatient(id, request);
        return ResponseEntity.ok(updatedPatient);
    }

    @Operation(summary = "Delete a patient")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "ID of the patient to be deleted", required = true)
            @PathVariable UUID id
    ) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
