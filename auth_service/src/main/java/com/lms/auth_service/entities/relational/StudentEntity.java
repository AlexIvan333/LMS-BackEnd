package com.lms.auth_service.entities.relational;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "students")
public class StudentEntity extends UserEntity {
}
