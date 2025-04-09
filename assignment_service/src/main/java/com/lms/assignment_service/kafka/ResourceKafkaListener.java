package com.lms.assignment_service.kafka;
import com.lms.shared.events.GetResourceResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResourceKafkaListener {
    private final ResourceRequestDispatcher dispatcher;

    @KafkaListener(topics = "resource-details-response", groupId = "lms-group")
    public void onResourceResponse(GetResourceResponseEvent event) {
        dispatcher.storeResponse(event.correlationId(), event.resources());
    }

}
