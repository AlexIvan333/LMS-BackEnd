package com.lms.backend.services;

import com.lms.backend.configurations.authentication.PasswordGenerator;
import com.lms.backend.dtos.filters.StudentFilterParams;
import com.lms.backend.dtos.requests.CreateUserRequest;
import com.lms.backend.dtos.responses.ServiceResult;
import com.lms.backend.dtos.responses.StudentResponse;
import com.lms.backend.domain.enums.Role;
import com.lms.backend.entities.relational.StudentEntity;
import com.lms.backend.mappers.StudentMapper;
import com.lms.backend.repositories.relational.StudentRepository;
import com.lms.backend.validation.interfaces.IUserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final IUserValidation userValidation;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final EmailService emailService;
    private final Google2FAService google2FAService;

   public ServiceResult<StudentResponse> registerStudent (CreateUserRequest request)
   {

       if (userValidation.HasValidEmail(request)) {
           return ServiceResult.<StudentResponse>builder()
                   .success(false)
                   .messageError("There already is an account with this email address.")
                   .httpStatus(HttpStatus.BAD_REQUEST)
                   .build();
       }

       String generatedPassword = passwordGenerator.generateRandomPassword();
       String encodedPassword = passwordEncoder.encode(generatedPassword);

       StudentEntity studentEntity = StudentEntity.builder()
               .firstName(request.getFirstName())
               .lastName(request.getLastName())
               .middleName(request.getMiddleName())
               .email(request.getEmail())
               .password(encodedPassword)
               .role(Role.STUDENT)
               .active(true)
               .streetName(request.getStreetName())
               .streetNumber(request.getStreetNumber())
               .country(request.getCountry())
               .city(request.getCity())
               .zipCode(request.getZipCode())
               .enrolledCourses(Collections.emptyList())
               .submissions(Collections.emptyList())
               .build();

       String secretKey = google2FAService.generateSecretKey();
       studentEntity.setTwoFactorSecretKey(secretKey);
       studentRepository.save(studentEntity);

       String qrCodeUrl = "otpauth://totp/LMS:" + studentEntity.getEmail() + "?secret=" + secretKey + "&issuer=LMS";

       emailService.sendEmailWithQRCode(
               studentEntity.getEmail(),
               "Welcome to LMS - Your Credentials",
               generatedPassword,
               qrCodeUrl
       );


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
               filterParams.getSubmissionID(),
               PageRequest.of(filterParams.getPage(), filterParams.getSize())).stream().toList();

       return studentEntities.stream().map(StudentMapper::toResponse).collect(Collectors.toList());

   }








}
