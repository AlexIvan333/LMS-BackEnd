package com.lms.shared.events;

import com.lms.shared.dtos.ResourceResponse;

import java.util.List;

public record GetResourceResponseEvent(List<ResourceResponse> resources, String correlationId) {}

