package com.lms.shared.events;

public record UserExistsResponseEvent(Long userId, boolean exists, String correlationId) {}
