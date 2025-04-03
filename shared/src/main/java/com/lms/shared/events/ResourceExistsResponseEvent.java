package com.lms.shared.events;

import java.util.List;

public record ResourceExistsResponseEvent(List<Long> validResourceIds, String correlationId) {}
