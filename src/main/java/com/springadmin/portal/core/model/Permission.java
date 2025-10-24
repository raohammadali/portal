package com.springadmin.portal.core.model;

import com.springadmin.portal.core.config.Auditable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "permissions")
@Data
@EqualsAndHashCode(callSuper = true)
public class Permission extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
}
