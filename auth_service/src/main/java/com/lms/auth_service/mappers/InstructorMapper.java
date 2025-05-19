package com.lms.auth_service.mappers;



import com.lms.auth_service.dtos.responses.InstructorResponse;
import com.lms.auth_service.entities.relational.InstructorEntity;

public class InstructorMapper {
    public static InstructorResponse toResponse(InstructorEntity entity) {
        if (entity == null) return null;

        return InstructorResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .middleName(entity.getMiddleName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .active(entity.getActive())
                .streetName(entity.getStreetName())
                .streetNumber(entity.getStreetNumber())
                .country(entity.getCountry())
                .city(entity.getCity())
                .zipCode(entity.getZipCode())
                .courseTitles(entity.getCourseTitles())
                .build();
    }
}
