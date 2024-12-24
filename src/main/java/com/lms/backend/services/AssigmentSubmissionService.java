package com.lms.backend.services;

import com.lms.backend.domain.enums.Grade;
import com.lms.backend.dtos.filters.AssigmentSubmissionFilterParams;
import com.lms.backend.dtos.requests.CreateAssigmentSubmissionRequest;
import com.lms.backend.dtos.responses.AssignmentSubmissionResponse;
import com.lms.backend.dtos.responses.ServiceResult;
import com.lms.backend.entities.relational.AssignmentSubmissionEntity;
import com.lms.backend.mappers.AssignmentSubmissionMapper;
import com.lms.backend.repositories.relational.AssignmentRepository;
import com.lms.backend.repositories.relational.AssignmentSubmissionRepository;
import com.lms.backend.repositories.relational.StudentRepository;
import com.lms.backend.validation.interfaces.IAssigmentSubmissionValidation;
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
public class AssigmentSubmissionService {
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;
    private final IAssigmentSubmissionValidation assigmentSubmissionValidation;

    public ServiceResult<AssignmentSubmissionResponse> createAssignmentSubmission(@RequestBody CreateAssigmentSubmissionRequest request) {

        if (!assigmentSubmissionValidation.isValid(request)) {
            return ServiceResult.<AssignmentSubmissionResponse>builder()
                    .success(false)
                    .messageError("The student or the assigment does not exist.")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        AssignmentSubmissionEntity assignmentSubmissionEntity = AssignmentSubmissionEntity.builder()
                .student(studentRepository.findStudentEntityById(request.getStudentId()))
                .assignment(assignmentRepository.findAssignmentEntitiesById(request.getAssigmentId()))
                .submissionTime(new Date())
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

    public List<AssignmentSubmissionResponse> getAssignmentSubmissions(AssigmentSubmissionFilterParams filterParams) {

        List<AssignmentSubmissionEntity> assignmentSubmissionEntities = assignmentSubmissionRepository.findAssignmentSubmissionsByFilters(
                filterParams.getSubmissionId(),
                filterParams.getStudentId(),
                filterParams.getAssigmentId(),
                filterParams.getCompleted(),
                filterParams.getGrade(),
                filterParams.getSubmissionTime(),
                PageRequest.of(filterParams.getPage(), filterParams.getSize())
        ).stream().toList();

        return assignmentSubmissionEntities.stream().map(AssignmentSubmissionMapper::toResponse).collect(Collectors.toList());
    }
}
