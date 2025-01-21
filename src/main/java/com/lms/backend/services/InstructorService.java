package com.lms.backend.services;

import com.lms.backend.configurations.authentication.PasswordGenerator;
import com.lms.backend.dtos.filters.InstructorFilterParams;
import com.lms.backend.dtos.requests.CreateUserRequest;
import com.lms.backend.dtos.responses.InstructorResponse;
import com.lms.backend.dtos.responses.ServiceResult;
import com.lms.backend.domain.enums.Role;
import com.lms.backend.entities.relational.CourseEntity;
import com.lms.backend.entities.relational.InstructorEntity;
import com.lms.backend.entities.relational.UserEntity;
import com.lms.backend.mappers.InstructorMapper;
import com.lms.backend.repositories.relational.InstructorRepository;
import com.lms.backend.repositories.relational.UserRepository;
import com.lms.backend.validation.interfaces.IUserValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final IUserValidation userValidation;
    private final EmailService emailService;
    private final Google2FAService google2FAService;

    public ServiceResult<InstructorResponse> registerInstructor(CreateUserRequest request)
    {
        if (userValidation.HasValidEmail(request.getEmail())) {
            return ServiceResult.<InstructorResponse>builder()
                    .success(false)
                    .messageError("There already is an account with this email address")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        String generatedPassword = passwordGenerator.generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(generatedPassword);

        InstructorEntity instructorEntity = InstructorEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .email(request.getEmail())
                .password(encodedPassword)
                .role(Role.INSTRUCTOR)
                .active(true)
                .streetName(request.getStreetName())
                .streetNumber(request.getStreetNumber())
                .country(request.getCountry())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .createdCourses(Collections.<CourseEntity>emptyList())
                .validatedByAdmin(false)
                .build();

        String secretKey = google2FAService.generateSecretKey();
        instructorEntity.setTwoFactorSecretKey(secretKey);
        instructorRepository.save(instructorEntity);

        String qrCodeUrl = "otpauth://totp/LMS:" + instructorEntity.getEmail() + "?secret=" + secretKey + "&issuer=LMS";

        emailService.sendEmailWithQRCode(
                instructorEntity.getEmail(),
                "Welcome to LMS - Your Credentials",
                generatedPassword,
                qrCodeUrl
        );

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

    public ServiceResult<Boolean> IsValidatedByAdmin(Long id)
    {
        if (!userValidation.IsIdValid(id))
        {
            return ServiceResult.<Boolean>builder()
                .success(false)
                .messageError("There is no user with this id in the database.")
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
        }
        if (!userValidation.IsInstructor(id))
        {
            return ServiceResult.<Boolean>builder()
                    .success(false)
                    .messageError("This use is not an Instructor")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        InstructorEntity instructorEntity = instructorRepository.getInstructorEntityById(id);
        return ServiceResult.<Boolean>builder()
                .data(instructorEntity.isValidatedByAdmin())
                .success(true)
                .httpStatus(HttpStatus.OK)
                .messageError(null)
                .build();
    }

    public Long getIdOFInstructorByEmail(String email)
    {
         return  userRepository.findByEmail(email).get().getId();
    }


}
