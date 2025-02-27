package com.lms.auth_service.dtos.responses;

import com.lms.auth_service.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AdminResponse {
    private Long id;
    private String email;
    private Role role;
}
