package com.lms.backend.controllers.responses;

import com.lms.backend.domain.enums.Role;
import com.lms.backend.domain.relational.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstructorResponse {
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
    private List<CourseResponse> createdCourses;
}
