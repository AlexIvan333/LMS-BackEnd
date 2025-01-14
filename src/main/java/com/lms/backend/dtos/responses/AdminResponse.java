package com.lms.backend.dtos.responses;

import com.lms.backend.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AdminResponse {
    private Long id;
    private String email;
    private String password;
    private Role role;
}
