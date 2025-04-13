package com.lms.assignment_service.services;


import com.lms.assignment_service.dtos.filters.AssignmentFilterParams;
import com.lms.assignment_service.dtos.requests.CreateAssignmentRequest;
import com.lms.assignment_service.dtos.responses.AssignmentResponse;
import com.lms.assignment_service.dtos.responses.ServiceResult;
import com.lms.assignment_service.entities.AssignmentEntity;
import com.lms.assignment_service.mappers.AssignmentMapper;
import com.lms.assignment_service.repositories.AssignmentRepository;
import com.lms.assignment_service.validation.interfaces.IAssignmentValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final IAssignmentValidation assigmentValidation;


    public ServiceResult<AssignmentResponse> createAssignment(CreateAssignmentRequest request) {

        if (!assigmentValidation.isValid(request)) {
            return ServiceResult.<AssignmentResponse>builder()
                    .success(false)
                    .messageError("The request is not valid.")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        AssignmentEntity assignmentEntity = AssignmentEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .courseId(request.getCourseId())
                .module_id(request.getModuleId())
                .resourceIds(request.getResourceIds())
                .build();

        assignmentRepository.save(assignmentEntity);

        return ServiceResult.<AssignmentResponse>builder()
                .data(AssignmentMapper.toResponse(assignmentEntity))
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public List<AssignmentResponse> getAssignments(AssignmentFilterParams filterParams) {

        List<AssignmentEntity> assignmentEntities = assignmentRepository.findAssignmentEntitiesByFilters(
                filterParams.getAssigmentId(),
                filterParams.getCourseId(),
                filterParams.getModuleId(),
                PageRequest.of(filterParams.getPage(), filterParams.getSize())
        ).stream().toList();

        return assignmentEntities.stream().map(AssignmentMapper::toResponse).collect(Collectors.toList()) ;
    }

}
