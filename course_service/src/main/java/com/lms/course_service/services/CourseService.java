package com.lms.course_service.services;

import com.lms.course_service.dtos.filters.CourseFilterParams;
import com.lms.course_service.dtos.requests.AssignStudentToCourseRequest;
import com.lms.course_service.dtos.requests.CreateCourseRequest;
import com.lms.course_service.dtos.responses.CourseResponse;
import com.lms.course_service.dtos.responses.ServiceResult;
import com.lms.course_service.entities.CourseStudentEntity;
import com.lms.course_service.mappers.CourseMapper;
import com.lms.course_service.entities.CourseEntity;
import com.lms.course_service.entities.ModuleEntity;
import com.lms.course_service.repositories.CourseRepository;
import com.lms.course_service.repositories.CourseStudentRepository;
import com.lms.shared.events.CheckUserExistsEvent;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public ServiceResult<CourseResponse> createCourse(CreateCourseRequest request) {

        String correlationId = UUID.randomUUID().toString();
        kafkaTemplate.send("user-validation-request",
                new CheckUserExistsEvent(request.getInstructorID(), "INSTRUCTOR", correlationId));

        CourseEntity courseEntity = CourseEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .modules(Collections.<ModuleEntity>emptyList())
                .instructorId(request.getInstructorID())
                .maxStudents(request.getMaxStudents())
                .build();

        courseRepository.save(courseEntity);

        return ServiceResult.<CourseResponse>builder()
                .data(CourseMapper.toResponse(courseEntity))
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public List<CourseResponse> getCourses(CourseFilterParams filterParams) {
        List<CourseEntity> courseEntities = courseRepository.findCourseEntitiesByFilters(
                filterParams.getCourseId(),
                filterParams.getInstructorId(),
                filterParams.getModuleId(),
                filterParams.getMaxStudents(),
                PageRequest.of(filterParams.getPage(), filterParams.getSize())
        ).stream().toList();

        return courseEntities.stream().map(CourseMapper ::toResponse).collect(Collectors.toList());
    }

    public ServiceResult<Boolean> assignStudentToCourse(AssignStudentToCourseRequest request) {
        Optional<CourseEntity> courseOptional = courseRepository.findCourseEntitiesById(request.getCourse_id());
        CourseEntity course = null;

        kafkaTemplate.send("user-validation-request",
                new CheckUserExistsEvent(request.getStudent_id(), "STUDENT", UUID.randomUUID().toString()));

        if(courseOptional.isPresent())
        {
            course = courseOptional.get();

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
        }
        else
        {
            return ServiceResult.<Boolean>builder()
                    .success(false)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .messageError("The course was not found")
                    .build();
        }




    }

}
