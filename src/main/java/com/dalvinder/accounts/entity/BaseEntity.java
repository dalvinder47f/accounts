package com.dalvinder.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@MappedSuperclass // for making this class as super class
@Getter @Setter @ToString // for creating getter setter
public class BaseEntity {

    @Column(updatable = false) // want to create only once
    private LocalDateTime createdAt;

    @Column(updatable = false)
    private String createdBy;

    @Column(insertable = false) // want to update only, not insert
    private LocalDateTime updatedAt;

    @Column(insertable = false)
    private String updatedBy;
}
