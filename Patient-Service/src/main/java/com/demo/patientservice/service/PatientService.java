package com.demo.patientservice.service;

import com.demo.patientservice.dto.request.PatientRequestDto;
import com.demo.patientservice.dto.response.PatientResponseDto;
import com.demo.patientservice.exceptions.DuplicateEmailException;
import com.demo.patientservice.exceptions.ResourceNotFoundException;
import com.demo.patientservice.grpc.BillingServiceGrpcClient;
import com.demo.patientservice.kafka.KafkaProducer;
import com.demo.patientservice.mapper.PatientMapper;
import com.demo.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public PatientResponseDto savePatient(PatientRequestDto request) {
        if (patientRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("Try another email!!");
        }
        var patientEntity = patientMapper.toEntity(request);
        var savedPatient = patientRepository.save(patientEntity);

        // Generate a billing account using gRPC
        billingServiceGrpcClient.createBillingAccount(savedPatient.getId().toString(), savedPatient.getName(), savedPatient.getEmail());

        // send a Kafka Event for a created patient
        kafkaProducer.sendEvent(savedPatient);

        return patientMapper.toDto(savedPatient);
    }

    public List<PatientResponseDto> getAllPatients() {
        var savedPatients = patientRepository.findAll();
        return savedPatients.stream().map(patientMapper::toDto).collect(Collectors.toList());
    }

    public PatientResponseDto getPatientById(UUID id) {
        var patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with above ID doesn't exist.."));
        return patientMapper.toDto(patient);
    }

    public PatientResponseDto updatePatient(UUID id, PatientRequestDto request) {
        var patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with above ID doesn't exist.."));
        patientMapper.updatePatient(patient, request);
        return patientMapper.toDto(patientRepository.save(patient));
    }

    public void deletePatient(UUID id) {
        var patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with above ID doesn't exist.."));
        patientRepository.delete(patient);
    }
}
