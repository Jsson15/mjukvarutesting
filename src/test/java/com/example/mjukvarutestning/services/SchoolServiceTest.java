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
    public void testCalculateAverageGrade() {
        List<Student> students = Arrays.asList(
                new Student("Alice", "Williams", LocalDate.of(2001, 1, 12), "alice.williams@example.com"),
                new Student("Bob", "Brown", LocalDate.of(2002, 3, 9), "bob.brown@example.com")
        );
        students.get(0).setJavaProgrammingGrade(4.0);
        students.get(1).setJavaProgrammingGrade(3.0);

        when(studentService.getAllStudents()).thenReturn(students);

        String averageGrade = schoolService.calculateAverageGrade();

        assertThat(averageGrade).isEqualTo("Average grade is 3.5");
    }

    @Test
    public void testCalculateAverageGradeNoStudents() {
        when(studentService.getAllStudents()).thenReturn(List.of());

        assertThatThrownBy(() -> schoolService.calculateAverageGrade())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No students found");
    }

    @Test
    public void testNumberOfStudentsPerGroupWhenDivideIntoNumberOfGroups() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("John", "Doe", LocalDate.of(2000, 5, 15), "john.doe@example.com"),
                new Student("Jane", "Doe", LocalDate.of(2001, 6, 20), "jane.doe@example.com"),
                new Student("Jim", "Beam", LocalDate.of(1999, 3, 11), "jim.beam@example.com")
        ));

        String result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(2);

        assertThat(result).contains("2 groups could be formed with 1 students per group, but that would leave 1 student hanging");
    }

    @Test
    public void testNumberOfStudentsPerGroupTooFewGroups() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("John", "Doe", LocalDate.of(2000, 5, 15), "john.doe@example.com")
        ));

        String result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(1);

        assertThat(result).isEqualTo("There should be at least two groups");
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
    public void testNumberOfGroupsWhenDividedIntoGroupsOfTooFewStudents() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("Alice", "Williams", LocalDate.of(2001, 1, 12), "alice.williams@example.com")
        ));

        String result = schoolService.numberOfGroupsWhenDividedIntoGroupsOf(2);

        assertThat(result).isEqualTo("Not able to manage groups of 2 with only 1 students");
    }
}
