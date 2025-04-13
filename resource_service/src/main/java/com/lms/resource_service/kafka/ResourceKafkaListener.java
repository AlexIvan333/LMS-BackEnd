package com.lms.resource_service.kafka;

import com.lms.resource_service.entites.ResourceEntity;
import com.lms.resource_service.repositories.ResourceRepository;
import com.lms.shared.events.CheckResourceExistsEvent;
import com.lms.shared.events.ResourceExistsResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResourceKafkaListener {

    private final ResourceRepository resourceRepo;

    @KafkaListener(topics = "resource-validation-request", groupId = "resource-service")
    @SendTo("resource-validation-response")
    public ResourceExistsResponseEvent onResourceValidation(CheckResourceExistsEvent event) {
        List<Long> valid = resourceRepo.findAllById(event.resourceIds())
                .stream().map(ResourceEntity::getId).toList();

        return new ResourceExistsResponseEvent(valid, event.correlationId());
    }
}
