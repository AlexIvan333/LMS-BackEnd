package com.lms.assignment_service.validation;


import com.lms.assignment_service.dtos.requests.CreateAssigmentRequest;
import com.lms.assignment_service.validation.interfaces.IAssigmentValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AssigmentValidation implements IAssigmentValidation {


    @Override
    public boolean isValid(CreateAssigmentRequest request) {
        return request.getModuleId() != null;
        //todo : add a check for the ids of the resource ids of the request
    }
}
