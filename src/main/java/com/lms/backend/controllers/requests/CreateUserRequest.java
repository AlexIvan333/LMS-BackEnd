package com.lms.backend.controllers.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String streetName;
    private int streetNumber;
    private String country;
    private String city;
    private String zipCode;
}
