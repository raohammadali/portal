package com.springadmin.portal.core.repositories;

import org.springframework.stereotype.Repository;
import com.springadmin.portal.core.model.User;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmailIgnoreCase(String email);
    Boolean existsByEmail(String email);
    Page<User> findAll(Pageable pageable);
}
