package com.lms.auth_service.validation.intrefaces;

public interface IUserValidator {
    boolean HasValidEmail(String email);
    boolean IsIdValid (Long id);
    boolean IsInstructor(Long id);
    boolean IsActive(String email);
}
