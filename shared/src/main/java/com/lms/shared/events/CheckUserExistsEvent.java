package com.lms.shared.events;


import com.lms.shared.dtos.Role;

public record CheckUserExistsEvent(Long userId, String role, String correlationId) {}
