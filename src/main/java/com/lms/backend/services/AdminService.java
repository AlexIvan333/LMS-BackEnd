package com.lms.backend.services;

import com.lms.backend.domain.enums.Role;
import com.lms.backend.dtos.requests.CreateUserRequest;
import com.lms.backend.dtos.responses.AdminResponse;
import com.lms.backend.dtos.responses.ServiceResult;
import com.lms.backend.entities.relational.AdminEntity;
import com.lms.backend.repositories.relational.AdminRepository;
import com.lms.backend.validation.interfaces.IUserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final IUserValidation userValidation;
    private final PasswordEncoder passwordEncoder;

    public ServiceResult<AdminResponse> createAdmin(CreateUserRequest request) {
        if (userValidation.HasValidEmail(request.getEmail())) {
            return ServiceResult.<AdminResponse>builder()
                    .success(false)
                    .messageError("There already is an account with this email address.")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        String encodedPassword = passwordEncoder.encode("password");
        AdminEntity adminEntity = AdminEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .email(request.getEmail())
                .password(encodedPassword)
                .role(Role.ADMIN)
                .active(true)
                .streetName(request.getStreetName())
                .streetNumber(request.getStreetNumber())
                .country(request.getCountry())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .department("Admin")
                .activityLogIds(Collections.emptyList())
                .build();
        adminRepository.save(adminEntity);

        return ServiceResult.<AdminResponse>builder()
                .data( AdminResponse.builder()
                        .id(adminEntity.getId())
                        .email(adminEntity.getEmail())
                        .password(adminEntity.getPassword())
                        .role(adminEntity.getRole())
                        .build())
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .messageError(null)
                .build();

    }
}
