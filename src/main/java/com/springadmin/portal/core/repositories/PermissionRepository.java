package com.springadmin.portal.core.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.springadmin.portal.core.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>{
    List<Permission> findAllByNameIn(List<String> names);
    Optional<Permission> findByName(String name);
    List<Permission> findByIdIn(List<Long> ids);
}
