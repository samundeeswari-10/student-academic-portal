package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.entity.Faculty;
import com.studentportal.studentacademicportal.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty saveFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public Optional<Faculty> getFacultyById(Long id) {
        return facultyRepository.findById(id);
    }

    public Faculty updateFaculty(Long id, Faculty updatedFaculty) {

        Optional<Faculty> existing =
                facultyRepository.findById(id);

        if (existing.isPresent()) {

            Faculty faculty = existing.get();

            faculty.setName(updatedFaculty.getName());
            faculty.setEmail(updatedFaculty.getEmail());
            faculty.setDepartment(updatedFaculty.getDepartment());
            faculty.setDesignation(updatedFaculty.getDesignation());

            return facultyRepository.save(faculty);
        }

        return null;
    }

    public boolean deleteFaculty(Long id) {

        if (!facultyRepository.existsById(id)) {
            return false;
        }

        facultyRepository.deleteById(id);
        return true;
    }
}