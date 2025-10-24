CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    roles_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, roles_id),

    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role FOREIGN KEY (roles_id)
        REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB;
