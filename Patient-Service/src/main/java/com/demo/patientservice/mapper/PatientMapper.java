package com.demo.patientservice.mapper;

import com.demo.patientservice.dto.request.PatientRequestDto;
import com.demo.patientservice.dto.response.PatientResponseDto;
import com.demo.patientservice.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patient toEntity(PatientRequestDto patientDto);

    PatientResponseDto toDto(Patient patientEntity);

    void updatePatient(@MappingTarget Patient patient, PatientRequestDto request);
}
