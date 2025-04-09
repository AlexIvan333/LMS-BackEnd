package com.lms.assignment_service.kafka;
import com.lms.shared.dtos.ResourceResponse;
import com.lms.shared.events.CheckResourceExistsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ResourceRequestDispatcher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final Map<String, List<ResourceResponse>> responseMap = new ConcurrentHashMap<>();

    public String requestResources(List<Long> ids) {
        String correlationId = UUID.randomUUID().toString();

        kafkaTemplate.send("resource-details-request", new CheckResourceExistsEvent(ids, correlationId));
        return correlationId;
    }

    public void storeResponse(String correlationId, List<ResourceResponse> resources) {
        responseMap.put(correlationId, resources);
    }

    public List<ResourceResponse> getResponseIfAvailable(String correlationId) {
        return responseMap.getOrDefault(correlationId, List.of());
    }
}
