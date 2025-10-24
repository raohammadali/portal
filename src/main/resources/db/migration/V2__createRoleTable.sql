CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,


    created_at DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME
) ENGINE=InnoDB;
