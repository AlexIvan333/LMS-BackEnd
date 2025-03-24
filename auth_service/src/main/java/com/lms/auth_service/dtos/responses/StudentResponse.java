package com.lms.auth_service.dtos.responses;

import com.lms.auth_service.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private Role role;
    private Boolean active;
    private String streetName;
    private int streetNumber;
    private String country;
    private String city;
    private String zipCode;
}
