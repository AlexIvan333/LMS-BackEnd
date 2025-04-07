package com.lms.shared.events;


public record CheckUserExistsEvent(Long userId, String role, String correlationId) {}
