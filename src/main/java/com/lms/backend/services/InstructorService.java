package com.lms.backend.services;

import com.lms.backend.configurations.authentication.PasswordGenerator;
import com.lms.backend.dtos.filters.InstructorFilterParams;
import com.lms.backend.dtos.requests.CreateUserRequest;
import com.lms.backend.dtos.responses.InstructorResponse;
import com.lms.backend.dtos.responses.ServiceResult;
import com.lms.backend.domain.enums.Role;
import com.lms.backend.entities.relational.CourseEntity;
import com.lms.backend.entities.relational.InstructorEntity;
import com.lms.backend.mappers.InstructorMapper;
import com.lms.backend.repositories.relational.InstructorRepository;
import com.lms.backend.validation.interfaces.IUserValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final IUserValidation userValidation;

    public ServiceResult<InstructorResponse> registerInstructor(CreateUserRequest request)
    {
        if (userValidation.HasValidEmail(request)) {
            return ServiceResult.<InstructorResponse>builder()
                    .success(false)
                    .messageError("There already is an account with this email address")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

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

        return ServiceResult.<InstructorResponse>builder()
                .data(InstructorMapper.toResponse(instructorEntity))
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .messageError(null)
                .build();
    }

    public List<InstructorResponse> getInstructors(InstructorFilterParams filterParams) {

            List<InstructorEntity> instructorEntities = instructorRepository.findInstructorEntitiesByFilters(
                    filterParams.getInstructorID(),
                    filterParams.getActive(),
                    filterParams.getCourseID(),
                    PageRequest.of(filterParams.getPage(), filterParams.getSize())).stream().toList();

            return instructorEntities.stream().map(InstructorMapper::toResponse).collect(Collectors.toList());
    }
}
