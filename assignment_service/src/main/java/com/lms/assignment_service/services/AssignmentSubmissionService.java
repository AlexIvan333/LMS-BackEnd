package com.lms.assignment_service.services;


import com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams;
import com.lms.assignment_service.dtos.requests.CreateAssignmentSubmissionRequest;
import com.lms.assignment_service.dtos.requests.GradeAssignmentSubmissionRequest;
import com.lms.assignment_service.dtos.responses.AssignmentSubmissionResponse;
import com.lms.assignment_service.dtos.responses.ServiceResult;
import com.lms.assignment_service.entities.AssignmentSubmissionEntity;
import com.lms.assignment_service.entities.Grade;
import com.lms.assignment_service.mappers.AssignmentSubmissionMapper;
import com.lms.assignment_service.repositories.AssignmentRepository;
import com.lms.assignment_service.repositories.AssignmentSubmissionRepository;
import com.lms.assignment_service.validation.interfaces.IAssignmentSubmissionValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AssignmentSubmissionService {
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final IAssignmentSubmissionValidation assigmentSubmissionValidation;

    public ServiceResult<AssignmentSubmissionResponse> createAssignmentSubmission(@RequestBody CreateAssignmentSubmissionRequest request) {

        if (!assigmentSubmissionValidation.isValid(request)) {
            return ServiceResult.<AssignmentSubmissionResponse>builder()
                    .success(false)
                    .messageError("The student or the assigment does not exist.")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        AssignmentSubmissionEntity assignmentSubmissionEntity = AssignmentSubmissionEntity.builder()
                .student_id(request.getStudentId())
                .assignment(assignmentRepository.findAssignmentEntitiesById(request.getAssigmentId()))
                .submission_time(new Date())
                .resourceIds(request.getResourceIds())
                .grade(Grade.UNDEFINED)
                .completed(false)
                .comment("")
                .build();

        assignmentSubmissionRepository.save(assignmentSubmissionEntity);

        return ServiceResult.<AssignmentSubmissionResponse>builder()
                .data(AssignmentSubmissionMapper.toResponse(assignmentSubmissionEntity))
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public List<AssignmentSubmissionResponse> getAssignmentSubmissions(AssignmentSubmissionFilterParams filterParams) {
        List<AssignmentSubmissionEntity> list = assignmentSubmissionRepository.getAssignmentSubmissionsEntitiesByFilters(
                filterParams.getSubmissionId(),
                filterParams.getStudentId(),
                filterParams.getAssigmentId(),
                filterParams.getCompleted(),
                filterParams.getGrade(),
                PageRequest.of(filterParams.getPage(), filterParams.getSize())
        ).stream().toList();


        return list.stream()
                .map(AssignmentSubmissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ServiceResult<AssignmentSubmissionResponse> gradeAssignmentSubmission(GradeAssignmentSubmissionRequest request)
    {
        if (!assigmentSubmissionValidation.Exists(request.assignmentSubmissionId))
        {
            return ServiceResult.<AssignmentSubmissionResponse>builder()
                    .success(false)
                    .messageError("The the assigment does not exist.")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        AssignmentSubmissionEntity assignmentSubmissionEntity = assignmentSubmissionRepository.findAssignmentSubmissionEntityById(request.assignmentSubmissionId);

        assignmentSubmissionEntity.setGrade(request.getGrade());
        assignmentSubmissionEntity.setCompleted(request.getCompleted());
        assignmentSubmissionEntity.setComment(request.getComment());
        assignmentSubmissionRepository.save(assignmentSubmissionEntity);

        return ServiceResult.<AssignmentSubmissionResponse>builder()
                .data(AssignmentSubmissionMapper.toResponse(assignmentSubmissionEntity))
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }
}
