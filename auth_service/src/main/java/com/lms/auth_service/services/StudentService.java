package com.lms.auth_service.services;

import com.lms.auth_service.configurations.authetication.PasswordGenerator;
import com.lms.auth_service.dtos.filters.UserFilterParams;
import com.lms.auth_service.dtos.requests.CreateUserRequest;
import com.lms.auth_service.dtos.responses.ServiceResult;
import com.lms.auth_service.dtos.responses.StudentResponse;
import com.lms.auth_service.entities.enums.Role;
import com.lms.auth_service.entities.relational.StudentEntity;
import com.lms.auth_service.mappers.StudentMapper;
import com.lms.auth_service.repositories.relational.StudentRepository;
import com.lms.auth_service.validation.intrefaces.IUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final IUserValidator userValidation;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final Google2FAService google2FAService;
    private final EmailService emailService;

   public ServiceResult<StudentResponse> registerStudent (CreateUserRequest request)
   {

       if (userValidation.HasValidEmail(request.getEmail())) {
           return ServiceResult.<StudentResponse>builder()
                   .success(false)
                   .messageError("There already is an account with this email address.")
                   .httpStatus(HttpStatus.BAD_REQUEST)
                   .build();
       }

       String generatedPassword = passwordGenerator.generateRandomPassword();
       String encodedPassword = passwordEncoder.encode(generatedPassword);

       StudentEntity studentEntity = (StudentEntity) StudentEntity.builder()
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

   public List<StudentResponse> getStudents (UserFilterParams filterParams)
   {
       List<StudentEntity> studentEntities = studentRepository.findStudentEntitiesByFilters(
               filterParams.getStudentId(),
               filterParams.getActive(),
               filterParams.getEmail(),
               PageRequest.of(filterParams.getPage(), filterParams.getSize())).stream().toList();

       return studentEntities.stream().map(StudentMapper::toResponse).collect(Collectors.toList());

   }








}
