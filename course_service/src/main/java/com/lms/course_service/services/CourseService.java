package com.lms.course_service.services;

import com.lms.course_service.dtos.filters.CourseFilterParams;
import com.lms.course_service.dtos.requests.AssignStudentToCourseRequest;
import com.lms.course_service.dtos.requests.CreateCourseRequest;
import com.lms.course_service.dtos.responses.CourseResponse;
import com.lms.course_service.dtos.responses.ServiceResult;
import com.lms.course_service.entities.CourseStudentEntity;
import com.lms.course_service.mappers.CourseMapper;
import com.lms.course_service.entities.CourseEntity;
import com.lms.course_service.repositories.CourseRepository;
import com.lms.course_service.repositories.CourseStudentRepository;
import com.lms.shared.events.CheckUserExistsEvent;
import com.lms.shared.events.UserExistsResponseEvent;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final ReplyingKafkaTemplate<String, Object, UserExistsResponseEvent> userReplyingKafkaTemplate;

    public ServiceResult<CourseResponse> createCourse(CreateCourseRequest request) {
        String correlationId = UUID.randomUUID().toString();
        CheckUserExistsEvent event = new CheckUserExistsEvent(request.getInstructorID(), "INSTRUCTOR", correlationId);

        try {
            ProducerRecord<String, Object> record = new ProducerRecord<>("user-validation-request", event);
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "user-validation-response".getBytes()));
            record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, correlationId.getBytes()));

            RequestReplyFuture<String, Object, UserExistsResponseEvent> future =
                    userReplyingKafkaTemplate.sendAndReceive(record);

            ConsumerRecord<String, UserExistsResponseEvent> response = future.get();

            if (response == null || response.value() == null || !response.value().exists()) {
                return ServiceResult.<CourseResponse>builder()
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .messageError("Instructor ID is invalid or validation failed")
                        .build();
            }

            if(!response.value().active())
            {
                return ServiceResult.<CourseResponse>builder()
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .messageError("Instructor is not active")
                        .build();
            }

            CourseEntity courseEntity = CourseEntity.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .modules(Collections.emptyList())
                    .instructorId(request.getInstructorID())
                    .maxStudents(request.getMaxStudents())
                    .build();

            courseRepository.save(courseEntity);

            return ServiceResult.<CourseResponse>builder()
                    .data(CourseMapper.toResponse(courseEntity))
                    .success(true)
                    .httpStatus(HttpStatus.CREATED)
                    .build();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ServiceResult.<CourseResponse>builder()
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .messageError("Kafka request interrupted: " + e.getMessage())
                    .build();
        } catch (ExecutionException e) {
            return ServiceResult.<CourseResponse>builder()
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .messageError("Kafka execution failed: " + e.getMessage())
                    .build();
        }
    }

    public List<CourseResponse> getCourses(CourseFilterParams filterParams) {
        List<CourseEntity> courseEntities = courseRepository.findCourseEntitiesByFilters(
                filterParams.getCourseId(),
                filterParams.getInstructorId(),
                filterParams.getModuleId(),
                filterParams.getMaxStudents(),
                PageRequest.of(filterParams.getPage(), filterParams.getSize())
        ).stream().toList();

        return courseEntities.stream().map(CourseMapper::toResponse).collect(Collectors.toList());
    }

    public ServiceResult<Boolean> assignStudentToCourse(AssignStudentToCourseRequest request) {
        try {
            Optional<CourseEntity> courseOptional = courseRepository.findCourseEntitiesById(request.getCourse_id());

            if (courseOptional.isEmpty()) {
                return ServiceResult.<Boolean>builder()
                        .success(false)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .messageError("The course was not found")
                        .build();
            }

            String correlationId = UUID.randomUUID().toString();
            CheckUserExistsEvent event = new CheckUserExistsEvent(request.getStudent_id(), "STUDENT", correlationId);

            ProducerRecord<String, Object> record = new ProducerRecord<>("user-validation-request", event);
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "user-validation-response".getBytes()));
            record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, correlationId.getBytes()));

            RequestReplyFuture<String, Object, UserExistsResponseEvent> future =
                    userReplyingKafkaTemplate.sendAndReceive(record);
            ConsumerRecord<String, UserExistsResponseEvent> response = future.get();

            if (response == null || response.value() == null || !response.value().exists()) {
                return ServiceResult.<Boolean>builder()
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .messageError("Student ID is invalid or validation failed")
                        .build();
            }

            if(!response.value().active())
            {
                return ServiceResult.<Boolean>builder()
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .messageError("Student is not active")
                        .build();
            }

            CourseEntity course = courseOptional.get();
            CourseStudentEntity courseStudentEntity = CourseStudentEntity.builder()
                    .courseId(course.getId())
                    .studentId(request.getStudent_id())
                    .build();

            courseStudentRepository.save(courseStudentEntity);

            return ServiceResult.<Boolean>builder()
                    .data(true)
                    .success(true)
                    .httpStatus(HttpStatus.CREATED)
                    .build();

        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return ServiceResult.<Boolean>builder()
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .messageError("Kafka request failed: " + e.getMessage())
                    .build();
        }
    }
}
