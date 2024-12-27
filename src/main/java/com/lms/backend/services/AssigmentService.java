package com.lms.backend.services;

import com.lms.backend.dtos.filters.AssigmentFilterParams;
import com.lms.backend.dtos.requests.CreateAssigmentRequest;
import com.lms.backend.dtos.responses.AssignmentResponse;
import com.lms.backend.dtos.responses.ServiceResult;
import com.lms.backend.entities.nosql.ResourceEntity;
import com.lms.backend.entities.relational.AssignmentEntity;
import com.lms.backend.helpers.ResourceHelperService;
import com.lms.backend.mappers.AssignmentMapper;
import com.lms.backend.repositories.nosql.ResourceRepository;
import com.lms.backend.repositories.relational.AssignmentRepository;
import com.lms.backend.repositories.relational.ModuleRepository;
import com.lms.backend.validation.interfaces.IAssigmentValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AssigmentService {
    private final AssignmentRepository assignmentRepository;
    private final ModuleRepository moduleRepository;
    private final IAssigmentValidation assigmentValidation;
    private final ResourceRepository resourceRepository;
    private final ResourceHelperService resourceHelperService;


    public ServiceResult<AssignmentResponse> createAssigment(CreateAssigmentRequest request) {

        if (!assigmentValidation.isValid(request)) {
            return ServiceResult.<AssignmentResponse>builder()
                    .success(false)
                    .messageError("The selected module does not exist.")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        List<ResourceEntity> resources = resourceHelperService.populateResources(request.getResourceIds());

        AssignmentEntity assignmentEntity = AssignmentEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .module(moduleRepository.findModuleEntityById(request.getModuleId()))
                .resourceIds(request.getResourceIds())
                .resources(resources != null ? resources : Collections.emptyList())
                .build();

        assignmentRepository.save(assignmentEntity);

        return ServiceResult.<AssignmentResponse>builder()
                .data(AssignmentMapper.toResponse(assignmentEntity))
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public List<AssignmentResponse> getAssigments(AssigmentFilterParams filterParams) {

        List<AssignmentEntity> assignmentEntities = assignmentRepository.findAssignmentEntitiesByFilters(
                filterParams.getAssigmentId(),
                filterParams.getCourseId(),
                filterParams.getModuleId(),
                PageRequest.of(filterParams.getPage(), filterParams.getSize())
        ).stream().toList();


        assignmentEntities.forEach(assignment -> {
            List<ResourceEntity> resources = resourceHelperService.populateResources(assignment.getResourceIds());
            assignment.setResources(resources != null ? resources : Collections.emptyList());
        });
        return assignmentEntities.stream().map(AssignmentMapper::toResponse).collect(Collectors.toList()) ;
    }
}
