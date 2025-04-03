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
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ServiceResult<ModuleResponse> createModule(CreateModuleRequest request) {

        Optional<CourseEntity> courseOptional = courseRepository.findCourseEntitiesById(request.getCourseId());
        CourseEntity course = null;

        if (courseOptional.isPresent()) {
            course = courseOptional.get();
        } else {
            return ServiceResult.<ModuleResponse>builder()
                    .success(false)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .messageError("Course not found for ID: " + request.getCourseId())
                    .build();
        }

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


        return moduleEntities.stream().map(ModuleMapper::toResponse).collect(Collectors.toList());
    }

    public ServiceResult<ModuleResponse> addResourceToModule(AddResourceToModuleRequest request ) {

        if (!moduleRepository.existsById(request.getModuleId())) {
            return ServiceResult.<ModuleResponse>builder()
                    .success(false)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .messageError("Module not found for ID: " + request.getModuleId())
                    .build();
        }

        kafkaTemplate.send("resource-validation-request",
                new CheckResourceExistsEvent(List.of(request.getResourceId()), UUID.randomUUID().toString()));

        ModuleEntity moduleEntity = moduleRepository.findModuleEntityById(request.getModuleId());

        moduleEntity.getResourceIds().add(request.getResourceId());
        moduleRepository.save(moduleEntity);

        return ServiceResult.<ModuleResponse>builder()
                .data(ModuleMapper.toResponse(moduleEntity))
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
