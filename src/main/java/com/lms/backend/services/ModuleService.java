package com.lms.backend.services;

import com.lms.backend.dtos.filters.ModuleFilterParams;
import com.lms.backend.dtos.requests.CreateModuleRequest;
import com.lms.backend.dtos.responses.ModuleResponse;
import com.lms.backend.dtos.responses.ServiceResult;
import com.lms.backend.entities.nosql.ResourceEntity;
import com.lms.backend.entities.relational.AssignmentEntity;
import com.lms.backend.entities.relational.ModuleEntity;
import com.lms.backend.helpers.ResourceHelperService;
import com.lms.backend.mappers.ModuleMapper;
import com.lms.backend.repositories.nosql.ResourceRepository;
import com.lms.backend.repositories.relational.CourseRepository;
import com.lms.backend.repositories.relational.ModuleRepository;
import com.lms.backend.validation.interfaces.IModuleValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final IModuleValidator moduleValidator;
    private final ResourceRepository resourceRepository;
    private final ResourceHelperService resourceHelperService;

    public ServiceResult<ModuleResponse> createModule(CreateModuleRequest request) {

        if (!moduleValidator.isValid(request)) {
            return ServiceResult.<ModuleResponse>builder()
                    .success(false)
                    .messageError("The course does not exist in the system")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        List<ResourceEntity> resources = resourceHelperService.populateResources(request.getResourceIds());

        ModuleEntity moduleEntity = ModuleEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .assignments(Collections.<AssignmentEntity>emptyList())
                .resourceIds(request.getResourceIds())
                .resources(resources != null ? resources : Collections.emptyList())
                .course(courseRepository.findCourseEntitiesById(request.getCourseId()))
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

        moduleEntities.forEach(moduleEntity ->{
            List<ResourceEntity> resources = resourceHelperService.populateResources(moduleEntity.getResourceIds());
            moduleEntity.setResources(resources != null ? resources : Collections.emptyList());
        });


        return moduleEntities.stream().map(ModuleMapper::toResponse).collect(Collectors.toList());
    }
}
