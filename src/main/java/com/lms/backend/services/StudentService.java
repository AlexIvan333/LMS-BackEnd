package com.lms.backend.services;

import com.lms.backend.configurations.authentication.PasswordGenerator;
import com.lms.backend.dtos.filters.StudentFilterParams;
import com.lms.backend.dtos.requests.CreateUserRequest;
import com.lms.backend.dtos.responses.ServiceResult;
import com.lms.backend.dtos.responses.StudentResponse;
import com.lms.backend.domain.enums.Role;
import com.lms.backend.entities.relational.AssignmentSubmissionEntity;
import com.lms.backend.entities.relational.CourseEntity;
import com.lms.backend.entities.relational.StudentEntity;
import com.lms.backend.mappers.StudentMapper;
import com.lms.backend.repositories.relational.StudentRepository;
import com.lms.backend.validation.interfaces.IUserValidation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final IUserValidation userValidation;

   public ServiceResult<StudentResponse> registerStudent (CreateUserRequest request)
   {
       if (userValidation.HasValidEmail(request)) {
           return ServiceResult.<StudentResponse>builder()
                   .success(false)
                   .messageError("There already is an account with this email address")
                   .httpStatus(HttpStatus.BAD_REQUEST)
                   .build();
       }

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

       return ServiceResult.<StudentResponse>builder()
               .data(StudentMapper.toResponse(studentEntity))
               .success(true)
               .httpStatus(HttpStatus.CREATED)
               .messageError(null)
               .build();

   }

   public List<StudentResponse> getStudents (StudentFilterParams filterParams)
   {
       List<StudentEntity> studentEntities = studentRepository.findStudentEntitiesByFilters( filterParams.getStudentId(),
               filterParams.getActive(),
               filterParams.getCourseID(),
               filterParams.getSubmissionID());

       return studentEntities.stream().map(StudentMapper::toResponse).collect(Collectors.toList());

   }








}
