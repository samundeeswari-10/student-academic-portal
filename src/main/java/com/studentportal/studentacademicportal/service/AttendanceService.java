package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.entity.Attendance;
import com.studentportal.studentacademicportal.entity.Student;
import com.studentportal.studentacademicportal.entity.Subject;
import com.studentportal.studentacademicportal.repository.AttendanceRepository;
import com.studentportal.studentacademicportal.repository.StudentRepository;
import com.studentportal.studentacademicportal.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            StudentRepository studentRepository,
            SubjectRepository subjectRepository) {

        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    public Attendance createAttendance(
            Long studentId,
            Long subjectId,
            Attendance attendance) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new RuntimeException("Subject not found"));

        if (attendanceRepository
                .existsByStudentIdAndSubjectIdAndAttendanceDate(
                        studentId,
                        subjectId,
                        attendance.getAttendanceDate())) {

            throw new RuntimeException(
                    "Attendance already exists for this student, subject and date");
        }

        attendance.setStudent(student);
        attendance.setSubject(subject);

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getAttendanceBySubject(Long subjectId) {
        return attendanceRepository.findBySubjectId(subjectId);
    }

    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    public Attendance updateAttendance(
            Long id,
            Attendance updatedAttendance) {

        Optional<Attendance> existing =
                attendanceRepository.findById(id);

        if (existing.isEmpty()) {
            return null;
        }

        Attendance attendance = existing.get();

        attendance.setAttendanceDate(
                updatedAttendance.getAttendanceDate());

        attendance.setPresent(
                updatedAttendance.isPresent());

        return attendanceRepository.save(attendance);
    }

    public boolean deleteAttendance(Long id) {

        if (!attendanceRepository.existsById(id)) {
            return false;
        }

        attendanceRepository.deleteById(id);
        return true;
    }

    public double getAttendancePercentage(
            Long studentId,
            Long subjectId) {

        long total = attendanceRepository
                .countByStudentIdAndSubjectId(studentId, subjectId);

        if (total == 0) {
            return 0.0;
        }

        long present = attendanceRepository
                .countByStudentIdAndSubjectIdAndPresentTrue(
                        studentId,
                        subjectId);

        return (present * 100.0) / total;
    }
}