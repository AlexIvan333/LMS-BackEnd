package com.lms.assignment_service.services;


import com.lms.assignment_service.dtos.filters.AssigmentFilterParams;
import com.lms.assignment_service.dtos.requests.CreateAssigmentRequest;
import com.lms.assignment_service.dtos.responses.AssignmentResponse;
import com.lms.assignment_service.dtos.responses.ServiceResult;
import com.lms.assignment_service.entities.AssignmentEntity;
import com.lms.assignment_service.mappers.AssignmentMapper;
import com.lms.assignment_service.repositories.AssignmentRepository;
import com.lms.assignment_service.validation.interfaces.IAssigmentValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AssigmentService {
    private final AssignmentRepository assignmentRepository;
    private final IAssigmentValidation assigmentValidation;


    public ServiceResult<AssignmentResponse> createAssigment(CreateAssigmentRequest request) {

        if (!assigmentValidation.isValid(request)) {
            return ServiceResult.<AssignmentResponse>builder()
                    .success(false)
                    .messageError("The selected module does not exist.")
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

    public List<AssignmentResponse> getAssigments(AssigmentFilterParams filterParams) {

        List<AssignmentEntity> assignmentEntities = assignmentRepository.findAssignmentEntitiesByFilters(
                filterParams.getAssigmentId(),
                filterParams.getCourseId(),
                filterParams.getModuleId(),
                PageRequest.of(filterParams.getPage(), filterParams.getSize())
        ).stream().toList();

        return assignmentEntities.stream().map(AssignmentMapper::toResponse).collect(Collectors.toList()) ;
    }
}
