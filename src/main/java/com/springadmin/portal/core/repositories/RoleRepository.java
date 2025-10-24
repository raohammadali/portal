package com.springadmin.portal.core.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.springadmin.portal.core.model.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{
    List<Role> findByNameIn(List<String> names);
    Optional<Role> findByName(String name);
    @EntityGraph(attributePaths = {"permissions"})
    List<Role> findByIdIn(List<Long> ids);
}
