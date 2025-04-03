package com.lms.shared.events;

import java.util.List;

public record CheckResourceExistsEvent(List<Long> resourceIds, String correlationId) {}
