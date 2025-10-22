package com.springadmin.portal.admin.service;
import java.util.List;

import com.springadmin.portal.core.model.Employee;

public interface EmployeeServices {
    List<Employee> getAllEmployee();
    void save(Employee employee);
    Employee getById(Long id);
    void deleteViaId(long id);
}