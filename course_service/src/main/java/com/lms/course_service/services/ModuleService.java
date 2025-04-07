package com.lms.course_service.services;

import com.lms.course_service.dtos.filters.ModuleFilterParams;
import com.lms.course_service.dtos.requests.AddResourceToModuleRequest;
import com.lms.course_service.dtos.requests.CreateModuleRequest;
import com.lms.course_service.dtos.responses.ModuleResponse;
import com.lms.course_service.dtos.responses.ServiceResult;
import com.lms.course_service.entities.CourseEntity;
import com.lms.course_service.entities.ModuleEntity;
import com.lms.course_service.mappers.ModuleMapper;
import com.lms.course_service.repositories.CourseRepository;
import com.lms.course_service.repositories.ModuleRepository;
import com.lms.shared.events.CheckResourceExistsEvent;
import com.lms.shared.events.ResourceExistsResponseEvent;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final ReplyingKafkaTemplate<String, Object, ResourceExistsResponseEvent> resourceReplyingKafkaTemplate;

    public ServiceResult<ModuleResponse> createModule(CreateModuleRequest request) {
        Optional<CourseEntity> courseOptional = courseRepository.findCourseEntitiesById(request.getCourseId());

        if (courseOptional.isEmpty()) {
            return ServiceResult.<ModuleResponse>builder()
                    .success(false)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .messageError("Course not found for ID: " + request.getCourseId())
                    .build();
        }

        CourseEntity course = courseOptional.get();

        ModuleEntity moduleEntity = ModuleEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .resourceIds(request.getResourceIds())
                .course(course)
                .build();

        moduleRepository.save(moduleEntity);

        return ServiceResult.<ModuleResponse>builder()
                .data(ModuleMapper.toResponse(moduleEntity))
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public List<ModuleResponse> getModules(ModuleFilterParams filterParams) {
        List<ModuleEntity> moduleEntities = moduleRepository.findModulesEntitiesByFilters(
                filterParams.getModuleId(),
                filterParams.getCourseID(),
                PageRequest.of(filterParams.getPage(), filterParams.getSize())
        ).stream().toList();

        return moduleEntities.stream()
                .map(ModuleMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ServiceResult<ModuleResponse> addResourceToModule(AddResourceToModuleRequest request) {
        if (!moduleRepository.existsById(request.getModuleId())) {
            return ServiceResult.<ModuleResponse>builder()
                    .success(false)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .messageError("Module not found for ID: " + request.getModuleId())
                    .build();
        }

        try {
            String correlationId = UUID.randomUUID().toString();
            CheckResourceExistsEvent event = new CheckResourceExistsEvent(List.of(request.getResourceId()), correlationId);

            ProducerRecord<String, Object> record = new ProducerRecord<>("resource-validation-request", event);
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "resource-validation-response".getBytes()));
            record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, correlationId.getBytes()));

            RequestReplyFuture<String, Object, ResourceExistsResponseEvent> future =
                    resourceReplyingKafkaTemplate.sendAndReceive(record);

            ConsumerRecord<String, ResourceExistsResponseEvent> response = future.get();

            if (response == null || response.value() == null ||
                    !response.value().validResourceIds().contains(request.getResourceId())) {
                return ServiceResult.<ModuleResponse>builder()
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .messageError("Invalid resource ID: " + request.getResourceId())
                        .build();
            }


            ModuleEntity moduleEntity = moduleRepository.findModuleEntityById(request.getModuleId());

            if (moduleEntity != null && moduleEntity.getResourceIds().contains(request.getResourceId())) {
                return ServiceResult.<ModuleResponse>builder()
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .messageError("The resource ID: " + request.getResourceId() + " already exists!")
                        .build();
            }

            moduleEntity.getResourceIds().add(request.getResourceId());
            moduleRepository.save(moduleEntity);

            return ServiceResult.<ModuleResponse>builder()
                    .data(ModuleMapper.toResponse(moduleEntity))
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ServiceResult.<ModuleResponse>builder()
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .messageError("Kafka interrupted: " + e.getMessage())
                    .build();
        } catch (ExecutionException e) {
            return ServiceResult.<ModuleResponse>builder()
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .messageError("Kafka error: " + e.getMessage())
                    .build();
        }
    }
}