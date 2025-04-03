package com.lms.shared.events;

public record GenericResponseEvent(String correlationId, boolean exists) {}