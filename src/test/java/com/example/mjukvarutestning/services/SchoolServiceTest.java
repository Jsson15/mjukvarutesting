package com.example.mjukvarutestning.services;

import com.example.mjukvarutestning.entities.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SchoolServiceTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private SchoolService schoolService;

    @Test
    public void testNumberOfStudentsPerGroupWhenGroupsLessThanTwo() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("Alice", "Williams", LocalDate.of(2001, 1, 12), "alice.williams@example.com")
        ));

        String result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(1);
        assertThat(result).isEqualTo("There should be at least two groups");
    }

    @Test
    public void testNumberOfStudentsPerGroupWhenMoreGroupsThanStudents() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("Alice", "Williams", LocalDate.of(2001, 1, 12), "alice.williams@example.com")
        ));

        String result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(3);
        assertThat(result).isEqualTo("Not able to divide 1 students into 3 groups");
    }

    @Test
    public void testNumberOfStudentsPerGroupWithRemainder() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("Alice", "Williams", LocalDate.of(2001, 1, 12), "alice.williams@example.com"),
                new Student("Bob", "Brown", LocalDate.of(2002, 3, 9), "bob.brown@example.com"),
                new Student("Charlie", "Chaplin", LocalDate.of(1995, 4, 16), "charlie.chaplin@example.com")
        ));

        String result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(2);
        assertThat(result).isEqualTo("2 groups could be formed with 1 students per group, but that would leave 1 student hanging");
    }

    @Test
    public void testNumberOfGroupsWhenDividedIntoGroupsOf() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("Alice", "Williams", LocalDate.of(2001, 1, 12), "alice.williams@example.com"),
                new Student("Bob", "Brown", LocalDate.of(2002, 3, 9), "bob.brown@example.com"),
                new Student("Charlie", "Chaplin", LocalDate.of(1995, 4, 16), "charlie.chaplin@example.com"),
                new Student("Diana", "Prince", LocalDate.of(1997, 7, 1), "diana.prince@example.com")
        ));

        String result = schoolService.numberOfGroupsWhenDividedIntoGroupsOf(2);
        assertThat(result).isEqualTo("2 students per group is possible, there will be 2 groups");
    }

    @Test
    public void testCalculateAverageGradeWithStudents() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("Alice", "Williams", LocalDate.of(2001, 1, 12), "alice.williams@example.com", 4.0),
                new Student("Bob", "Brown", LocalDate.of(2002, 3, 9), "bob.brown@example.com", 3.0)
        ));

        String result = schoolService.calculateAverageGrade();
        assertThat(result).isEqualTo("Average grade is 3.5");
    }

    @Test
    public void testCalculateAverageGradeNoStudents() {
        when(studentService.getAllStudents()).thenReturn(List.of());

        assertThatThrownBy(() -> schoolService.calculateAverageGrade())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No students found");
    }

    @Test
    public void testGetTopScoringStudents() {
        Student student1 = new Student("Alice", "Williams", LocalDate.of(2001, 1, 12), "alice.williams@example.com");
        student1.setJavaProgrammingGrade(4.5);

        Student student2 = new Student("Bob", "Brown", LocalDate.of(2002, 3, 9), "bob.brown@example.com");
        student2.setJavaProgrammingGrade(3.5);

        when(studentService.getAllStudents()).thenReturn(Arrays.asList(student1, student2));

        List<Student> topStudents = schoolService.getTopScoringStudents();
        assertThat(topStudents).hasSize(1);
        assertThat(topStudents.get(0).getJavaProgrammingGrade()).isEqualTo(4.5);
    }

    @Test
    public void testGetTopScoringStudentsNoStudents() {
        when(studentService.getAllStudents()).thenReturn(List.of());

        assertThatThrownBy(() -> schoolService.getTopScoringStudents())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No students found");
    }
}
