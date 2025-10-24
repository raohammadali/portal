CREATE TABLE user_permissions (
    user_id BIGINT NOT NULL,
    permissions_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, permissions_id),

    CONSTRAINT fk_user_permissions_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_user_permissions_permission FOREIGN KEY (permissions_id)
        REFERENCES permissions(id) ON DELETE CASCADE
) ENGINE=InnoDB;
