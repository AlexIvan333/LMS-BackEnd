package com.lms.shared.events;

public record GetInstructorDetailsEvent(Long instructorId, String correlationId) {}
