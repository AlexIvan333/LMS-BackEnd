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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;
    private final IUserValidation userValidation;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final EmailService emailService;
    private final Google2FAService google2FAService;

   public ServiceResult<StudentResponse> registerStudent (CreateUserRequest request)
   {
       log.info("Registering new student with email: {}", request.getEmail());

       if (userValidation.HasValidEmail(request)) {
           log.warn("Attempt to register with an existing email: {}", request.getEmail());
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
       studentEntity.setTwoFactorSecretKey(secretKey); // Ensure the field exists in UserEntity
       studentRepository.save(studentEntity);

// Optionally send a QR code link for the user
       String qrCodeUrl = "otpauth://totp/LMS:" + studentEntity.getEmail()
               + "?secret=" + secretKey + "&issuer=LMS";
       emailService.sendEmail(
               studentEntity.getEmail(),
               "Welcome to LMS - Your Credentials",
               "Your account has been created. Use the password: " + generatedPassword +
                       "\n\nFor added security, set up 2FA by scanning this QR Code URL: " + qrCodeUrl);

       log.info("Student registered successfully: {}", studentEntity.getEmail());


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
