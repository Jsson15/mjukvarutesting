package com.example.mjukvarutestning.repo;

import com.example.mjukvarutestning.entities.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepoTest {

    @Autowired
    private StudentRepo studentRepo;

    @Test
    public void testSaveAndFindById() {
        // Arrange
        Student student = new Student("John", "Doe", LocalDate.of(2000, 5, 15), "john.doe@example.com");
        student.setJavaProgrammingGrade(4.5);

        // Act
        studentRepo.save(student);
        Optional<Student> foundStudent = studentRepo.findById(student.getId());

        // Assert
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void testExistsStudentByEmail() {
        // Arrange
        Student student = new Student("Jane", "Smith", LocalDate.of(1999, 8, 22), "jane.smith@example.com");
        studentRepo.save(student);

        // Act
        boolean exists = studentRepo.existsStudentByEmail("jane.smith@example.com");
        boolean notExists = studentRepo.existsStudentByEmail("not.exist@example.com");

        // Assert
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    public void testDeleteStudent() {
        // Arrange
        Student student = new Student("Mark", "Johnson", LocalDate.of(1998, 2, 14), "mark.johnson@example.com");
        studentRepo.save(student);
        int studentId = student.getId();

        // Act
        studentRepo.deleteById(studentId);
        Optional<Student> foundStudent = studentRepo.findById(studentId);

        // Assert
        assertThat(foundStudent).isNotPresent();
    }

    @Test
    public void testFindAllStudents() {
        // Arrange
        Student student1 = new Student("Alice", "Williams", LocalDate.of(2001, 1, 12), "alice.williams@example.com");
        Student student2 = new Student("Bob", "Brown", LocalDate.of(2002, 3, 9), "bob.brown@example.com");
        studentRepo.save(student1);
        studentRepo.save(student2);

        // Act
        Iterable<Student> students = studentRepo.findAll();

        // Assert
        assertThat(students).hasSize(2);
    }

    @Test
    public void testFindById() {
        // Arrange
        Student student = new Student("Charlie", "Chaplin", LocalDate.of(1995, 4, 16), "charlie.chaplin@example.com");
        studentRepo.save(student);

        // Act
        Optional<Student> foundStudent = studentRepo.findById(student.getId());

        // Assert
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getFirstName()).isEqualTo("Charlie");
    }

    @Test
    public void testFindByIdNotFound() {
        // Act
        Optional<Student> foundStudent = studentRepo.findById(999);

        // Assert
        assertThat(foundStudent).isNotPresent();
    }

    @Test
    public void testUpdateStudent() {
        // Arrange
        Student student = new Student("Diana", "Prince", LocalDate.of(1997, 7, 1), "diana.prince@example.com");
        studentRepo.save(student);

        // Act
        student.setJavaProgrammingGrade(4.0);
        studentRepo.save(student);

        Optional<Student> updatedStudent = studentRepo.findById(student.getId());

        // Assert
        assertThat(updatedStudent).isPresent();
        assertThat(updatedStudent.get().getJavaProgrammingGrade()).isEqualTo(4.0);
    }
}
