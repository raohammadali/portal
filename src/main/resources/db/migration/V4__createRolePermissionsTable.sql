CREATE TABLE role_permissions (
    roles_id BIGINT NOT NULL,
    permissions_id BIGINT NOT NULL,
    PRIMARY KEY (roles_id, permissions_id),

    CONSTRAINT fk_role_permissions_role FOREIGN KEY (roles_id)
        REFERENCES roles(id) ON DELETE CASCADE,

    CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permissions_id)
        REFERENCES permissions(id) ON DELETE CASCADE
) ENGINE=InnoDB;
