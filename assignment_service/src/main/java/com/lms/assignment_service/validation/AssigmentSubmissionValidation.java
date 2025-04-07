package com.lms.assignment_service.validation;


import com.lms.assignment_service.dtos.requests.CreateAssigmentSubmissionRequest;
import com.lms.assignment_service.repositories.AssignmentRepository;
import com.lms.assignment_service.repositories.AssignmentSubmissionRepository;
import com.lms.assignment_service.validation.interfaces.IAssigmentSubmissionValidation;
import com.lms.shared.events.CheckResourceExistsEvent;
import com.lms.shared.events.CheckUserExistsEvent;
import com.lms.shared.events.ResourceExistsResponseEvent;
import com.lms.shared.events.UserExistsResponseEvent;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.List;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@Component
public class AssigmentSubmissionValidation implements IAssigmentSubmissionValidation {
    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final ReplyingKafkaTemplate<String, Object, UserExistsResponseEvent> userReplyingKafkaTemplate;
    private final ReplyingKafkaTemplate<String, Object, ResourceExistsResponseEvent> resourceReplyingKafkaTemplate;


    @Override
    public boolean isValid(CreateAssigmentSubmissionRequest request) {
        try {

            if (request.getStudentId() == null) return false;

            String userCorrelationId = UUID.randomUUID().toString();
            CheckUserExistsEvent userEvent = new CheckUserExistsEvent(request.getStudentId(), "STUDENT", userCorrelationId);

            ProducerRecord<String, Object> userRecord = new ProducerRecord<>("user-validation-request", userEvent);
            userRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "user-validation-response".getBytes()));
            userRecord.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, userCorrelationId.getBytes()));

            RequestReplyFuture<String, Object, UserExistsResponseEvent> userFuture =
                    userReplyingKafkaTemplate.sendAndReceive(userRecord);
            ConsumerRecord<String, UserExistsResponseEvent> userResponse = userFuture.get();

            if (userResponse == null || userResponse.value() == null || !userResponse.value().exists()) {
                return false;
            }


            if (!assignmentRepository.existsById(request.getAssigmentId())) return false;


            List<Long> resourceIds = request.getResourceIds();
            if (resourceIds != null && !resourceIds.isEmpty()) {
                String resCorrelationId = UUID.randomUUID().toString();
                CheckResourceExistsEvent resEvent = new CheckResourceExistsEvent(resourceIds, resCorrelationId);

                ProducerRecord<String, Object> resRecord = new ProducerRecord<>("resource-validation-request", resEvent);
                resRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "resource-validation-response".getBytes()));
                resRecord.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, resCorrelationId.getBytes()));

                RequestReplyFuture<String, Object, ResourceExistsResponseEvent> resFuture =
                        resourceReplyingKafkaTemplate.sendAndReceive(resRecord);

                ConsumerRecord<String, ResourceExistsResponseEvent> resResponse = resFuture.get();

                if (resResponse == null || resResponse.value() == null ||
                        resResponse.value().validResourceIds().size() != resourceIds.size()) {
                    return false;
                }
            }

            return true;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (ExecutionException e) {
            return false;
        }
    }

    @Override
    public boolean Exists(Long assigmentSubmissionId) {
        return assignmentSubmissionRepository.existsById(assigmentSubmissionId);
    }


}
