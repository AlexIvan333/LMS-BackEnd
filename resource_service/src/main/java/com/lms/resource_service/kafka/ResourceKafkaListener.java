package com.lms.resource_service.kafka;

import com.lms.resource_service.entites.ResourceEntity;
import com.lms.resource_service.repositories.ResourceRepository;
import com.lms.shared.events.CheckResourceExistsEvent;
import com.lms.shared.events.ResourceExistsResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResourceKafkaListener {

    private final ResourceRepository resourceRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    @KafkaListener(topics = "resource-validation-request", groupId = "lms-group")
    public void onResourceValidation(CheckResourceExistsEvent event) {
        List<Long> valid = resourceRepo.findAllById(event.resourceIds())
                .stream().map(ResourceEntity::getId).toList();
        ResourceExistsResponseEvent response = new ResourceExistsResponseEvent(valid, event.correlationId());
        kafkaTemplate.send("resource-validation-response", response);
    }
}
