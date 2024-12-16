package com.lms.backend.services;

import com.lms.backend.configurations.authentication.PasswordGenerator;
import com.lms.backend.controllers.requests.CreateUserRequest;
import com.lms.backend.controllers.responses.InstructorResponse;
import com.lms.backend.domain.enums.Role;
import com.lms.backend.entities.relational.CourseEntity;
import com.lms.backend.entities.relational.InstructorEntity;
import com.lms.backend.mappers.InstructorMapper;
import com.lms.backend.repositories.relational.InstructorRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;

    public InstructorResponse registerInstructor(CreateUserRequest request)
    {
        InstructorEntity instructorEntity = InstructorEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(passwordGenerator.generateRandomPassword()))
                .role(Role.INSTRUCTOR)
                .active(true)
                .streetName(request.getStreetName())
                .streetNumber(request.getStreetNumber())
                .country(request.getCountry())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .createdCourses(Collections.<CourseEntity>emptyList())
                .build();

        instructorRepository.save(instructorEntity);
        return InstructorMapper.toResponse(instructorEntity);
    }
}
