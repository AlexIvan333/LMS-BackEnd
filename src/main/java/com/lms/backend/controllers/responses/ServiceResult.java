package com.lms.backend.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceResult<T> {
    public T data;
    public boolean success;
    public String messageError;
    public HttpStatus httpStatus;
}
