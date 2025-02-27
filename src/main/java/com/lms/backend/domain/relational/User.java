package com.lms.backend.domain.relational;

import com.lms.backend.domain.enums.Role;
import lombok.Data;

@Data
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String password;
    private Role role;
    private Boolean active;
    //address fields
    private String streetName;
    private int streetNumber;
    private String country;
    private String city;
    private String zipCode;

}
