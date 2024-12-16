package com.lms.backend.services;

import com.lms.backend.configurations.authentication.PasswordGenerator;
import com.lms.backend.controllers.requests.CreateUserRequest;
import com.lms.backend.controllers.responses.InstructorResponse;
import com.lms.backend.controllers.responses.StudentResponse;
import com.lms.backend.domain.enums.Role;
import com.lms.backend.entities.relational.AssignmentSubmissionEntity;
import com.lms.backend.entities.relational.CourseEntity;
import com.lms.backend.entities.relational.InstructorEntity;
import com.lms.backend.entities.relational.StudentEntity;
import com.lms.backend.mappers.InstructorMapper;
import com.lms.backend.mappers.StudentMapper;
import com.lms.backend.repositories.relational.InstructorRepository;
import com.lms.backend.repositories.relational.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;

   public StudentResponse registerStudent (CreateUserRequest request)
   {
       StudentEntity studentEntity = StudentEntity.builder()
               .firstName(request.getFirstName())
               .lastName(request.getLastName())
               .middleName(request.getMiddleName())
               .email(request.getEmail())
               .password(passwordEncoder.encode(passwordGenerator.generateRandomPassword()))
               .role(Role.STUDENT)
               .active(true)
               .streetName(request.getStreetName())
               .streetNumber(request.getStreetNumber())
               .country(request.getCountry())
               .city(request.getCity())
               .zipCode(request.getZipCode())
               .enrolledCourses(Collections.<CourseEntity>emptyList())
               .submissions(Collections.<AssignmentSubmissionEntity>emptyList())
               .build();

       studentRepository.save(studentEntity);
       return StudentMapper.toResponse(studentEntity);
   }







}
