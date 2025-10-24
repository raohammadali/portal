CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(666),
    last_name VARCHAR(666),
    phone VARCHAR(15),
    two_factor_code VARCHAR(255),
    two_factor_expiry DATETIME,
    email VARCHAR(255),
    password VARCHAR(255),
    is_verified BOOLEAN,


    created_at DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME
) ENGINE=InnoDB;
