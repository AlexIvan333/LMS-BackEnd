package com.lms.backend.services;

import com.lms.backend.domain.relational.Course;
import com.lms.backend.dtos.filters.CourseFilterParams;
import com.lms.backend.dtos.requests.CreateCourseRequest;
import com.lms.backend.dtos.responses.CourseResponse;
import com.lms.backend.dtos.responses.CourseResponseForStudent;
import com.lms.backend.dtos.responses.ServiceResult;
import com.lms.backend.entities.relational.CourseEntity;
import com.lms.backend.entities.relational.ModuleEntity;
import com.lms.backend.entities.relational.StudentEntity;
import com.lms.backend.mappers.CourseMapper;
import com.lms.backend.repositories.relational.CourseRepository;
import com.lms.backend.repositories.relational.InstructorRepository;
import com.lms.backend.validation.interfaces.ICourseValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final ICourseValidation courseValidation;


    public ServiceResult<CourseResponse> createCourse(CreateCourseRequest request) {

        if (!courseValidation.validateCourse(request)) {
                return ServiceResult.<CourseResponse>builder()
                        .success(false)
                        .messageError("The maximum number of students needs to be greater than zero or the selected instructor does not exist.")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
        }
        CourseEntity courseEntity = CourseEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .modules(Collections.<ModuleEntity>emptyList())
                .instructor(instructorRepository.getInstructorEntityById(request.getInstructorID()))
                .enrolledStudents(Collections.<StudentEntity>emptyList())
                .maxStudents(request.getMaxStudents())
                .build();

        courseRepository.save(courseEntity);

        return ServiceResult.<CourseResponse>builder()
                .data(CourseMapper.ToCourseResponse(courseEntity))
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public List<CourseResponse> getCourses(CourseFilterParams filterParams) {
        List<CourseEntity> courseEntities = courseRepository.findCourseEntitiesByFilters(
                filterParams.getCourseId(),
                filterParams.getInstructorId(),
                filterParams.getStudentId(),
                filterParams.getModuleId(),
                filterParams.getMaxStudents(),
                PageRequest.of(filterParams.getPage(), filterParams.getSize())
        ).stream().toList();

        return courseEntities.stream().map(CourseMapper ::ToCourseResponse).collect(Collectors.toList());
    }

    public List<CourseResponseForStudent> getCoursesForStudents(CourseFilterParams filterParams) {
        List<CourseEntity> courseEntities = courseRepository.findCourseEntitiesByFilters(
                filterParams.getCourseId(),
                filterParams.getInstructorId(),
                filterParams.getStudentId(),
                filterParams.getModuleId(),
                filterParams.getMaxStudents(),
                PageRequest.of(filterParams.getPage(), filterParams.getSize())
        ).stream().toList();

        return courseEntities.stream().map(CourseMapper ::ToCourseResponseForStudents).collect(Collectors.toList());
    }
}
