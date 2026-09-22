package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.entity.Department;
import com.studentportal.studentacademicportal.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public Department updateDepartment(
            Long id,
            Department updatedDepartment) {

        Optional<Department> existing =
                departmentRepository.findById(id);

        if (existing.isEmpty()) {
            return null;
        }

        Department department = existing.get();

        department.setName(updatedDepartment.getName());
        department.setCode(updatedDepartment.getCode());

        return departmentRepository.save(department);
    }

    public boolean deleteDepartment(Long id) {

        if (!departmentRepository.existsById(id)) {
            return false;
        }

        departmentRepository.deleteById(id);
        return true;
    }
}