//package com.studentportal.studentacademicportal.service;
//
//public class StudentService {
//}
package com.studentportal.studentacademicportal.service;
import com.studentportal.studentacademicportal.entity.Student;
import java.util.List;
import com.studentportal.studentacademicportal.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Student updateStudent(Long id, Student updatedStudent) {

        Optional<Student> existingStudent = studentRepository.findById(id);

        if (existingStudent.isPresent()) {

            Student student = existingStudent.get();

            student.setName(updatedStudent.getName());
            student.setEmail(updatedStudent.getEmail());
            student.setDepartment(updatedStudent.getDepartment());
            student.setYear(updatedStudent.getYear());

            return studentRepository.save(student);
        }

        return null;
    }
}