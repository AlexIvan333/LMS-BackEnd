package com.lms.auth_service.entities.relational;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "admins")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class AdminEntity extends UserEntity {

    @Column(nullable = false)
    private String department;

}
