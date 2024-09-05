package com.example.mjukvarutestning.services;

import com.example.mjukvarutestning.entities.Student;
import com.example.mjukvarutestning.repo.StudentRepo;
import com.example.mjukvarutestning.services.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepo studentRepo;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testAddStudent() {
        Student student = new Student("Alice", "Williams", LocalDate.of(2001, 1, 12), "alice.williams@example.com");

        when(studentRepo.existsStudentByEmail(student.getEmail())).thenReturn(false);
        when(studentRepo.save(student)).thenReturn(student);

        Student savedStudent = studentService.addStudent(student);

        assertThat(savedStudent.getEmail()).isEqualTo("alice.williams@example.com");
        verify(studentRepo).save(student);
    }

    @Test
    public void testAddStudentEmailAlreadyExists() {
        Student student = new Student("Bob", "Brown", LocalDate.of(2002, 3, 9), "bob.brown@example.com");

        when(studentRepo.existsStudentByEmail(student.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> studentService.addStudent(student))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Email bob.brown@example.com already exists");

        verify(studentRepo, never()).save(student);
    }

    @Test
    public void testGetStudentByIdNotFound() {
        when(studentRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(1))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Could not find student by id 1");
    }

    @Test
    public void testUpdateStudent() {
        Student student = new Student("Alice", "Williams", LocalDate.of(2001, 1, 12), "alice.williams@example.com");
        student.setId(1);

        when(studentRepo.existsById(student.getId())).thenReturn(true);
        when(studentRepo.save(student)).thenReturn(student);

        Student updatedStudent = studentService.updateStudent(student);

        assertThat(updatedStudent).isNotNull();
        assertThat(updatedStudent.getEmail()).isEqualTo("alice.williams@example.com");
    }

    @Test
    public void testUpdateStudentNotFound() {
        Student student = new Student("Bob", "Dylan", LocalDate.of(2000, 8, 20), "bob.dylan@example.com");
        student.setId(2);

        when(studentRepo.existsById(student.getId())).thenReturn(false);

        assertThatThrownBy(() -> studentService.updateStudent(student))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Could not find and update student by id " + student.getId());

        verify(studentRepo, never()).save(student);
    }

    @Test
    public void testDeleteStudent() {
        when(studentRepo.existsById(1)).thenReturn(true);

        studentService.deleteStudent(1);

        verify(studentRepo).deleteById(1);
    }

    @Test
    public void testDeleteStudentNotFound() {
        when(studentRepo.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> studentService.deleteStudent(1))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Could not find and delete student by id 1");

        verify(studentRepo, never()).deleteById(1);
    }

    @Test
    public void testSetGradeForStudentById() {
        Student student = new Student("Chris", "Evans", LocalDate.of(1996, 2, 13), "chris.evans@example.com");
        student.setId(3);

        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
        when(studentRepo.save(student)).thenReturn(student);

        Student updatedStudent = studentService.setGradeForStudentById(student.getId(), "4.5");

        assertThat(updatedStudent.getJavaProgrammingGrade()).isEqualTo(4.5);
    }

    @Test
    public void testSetGradeForStudentByIdInvalidGrade() {
        assertThatThrownBy(() -> studentService.setGradeForStudentById(1, "invalid"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Valid grades are 0.0 - 5.0");

        assertThatThrownBy(() -> studentService.setGradeForStudentById(1, "6.0"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Valid grades are 0.0 - 5.0");
    }

    @Test
    public void testGetAllStudents() {
        when(studentRepo.findAll()).thenReturn(Arrays.asList(
                new Student("Alice", "Williams", LocalDate.of(2001, 1, 12), "alice.williams@example.com"),
                new Student("Bob", "Brown", LocalDate.of(2002, 3, 9), "bob.brown@example.com")
        ));

        List<Student> students = studentService.getAllStudents();

        assertThat(students).hasSize(2);
        verify(studentRepo).findAll();
    }
}
