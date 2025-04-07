package com.lms.assignment_service.validation;


import com.lms.assignment_service.dtos.requests.CreateAssigmentRequest;
import com.lms.assignment_service.validation.interfaces.IAssigmentValidation;
import com.lms.shared.events.CheckResourceExistsEvent;
import com.lms.shared.events.ResourceExistsResponseEvent;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.List;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@Component
public class AssigmentValidation implements IAssigmentValidation {

    private final ReplyingKafkaTemplate<String, Object, ResourceExistsResponseEvent> resourceReplyingKafkaTemplate;


    @Override
    public boolean isValid(CreateAssigmentRequest request) {
        if (request.getModuleId() == null) return false;

        List<Long> resourceIds = request.getResourceIds();
        if (resourceIds != null && !resourceIds.isEmpty()) {
            try {
                String correlationId = UUID.randomUUID().toString();
                CheckResourceExistsEvent event = new CheckResourceExistsEvent(resourceIds, correlationId);

                ProducerRecord<String, Object> record = new ProducerRecord<>("resource-validation-request", event);
                record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "resource-validation-response".getBytes()));
                record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, correlationId.getBytes()));

                RequestReplyFuture<String, Object, ResourceExistsResponseEvent> future =
                        resourceReplyingKafkaTemplate.sendAndReceive(record);

                ConsumerRecord<String, ResourceExistsResponseEvent> response = future.get();

                if (response == null || response.value() == null ||
                        response.value().validResourceIds().size() != resourceIds.size()) {
                    return false;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            } catch (ExecutionException e) {
                return false;
            }
        }

        return true;
    }
}
