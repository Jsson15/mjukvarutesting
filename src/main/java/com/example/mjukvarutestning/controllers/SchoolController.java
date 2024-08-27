package com.example.mjukvarutestning.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.mjukvarutestning.entities.Student;
import com.example.mjukvarutestning.services.SchoolService;
import com.example.mjukvarutestning.services.StudentService;

import java.util.List;

@RestController
@RequestMapping("/edu/api/v1/school")
public class SchoolController {

    private final StudentService studentService;
    private final SchoolService schoolService;

    public SchoolController(StudentService studentService, SchoolService schoolService) {
        this.studentService = studentService;
        this.schoolService = schoolService;
    }

    @GetMapping("/countstudents")
    public ResponseEntity<Integer> countStudents(){
        return ResponseEntity.ok(studentService.getAllStudents().size());
    }
    @GetMapping("/divideintogroupsof/{n}")
    public ResponseEntity<String> getNumberOfGroupsWhenNStudentsPerGroup(@PathVariable int n){
        return ResponseEntity.ok(schoolService.numberOfGroupsWhenDividedIntoGroupsOf(n));
    }
    @GetMapping("/createnumberofgroups/{n}")
    public ResponseEntity<String> getStudentsPerGroup(@PathVariable int n){
        return ResponseEntity.ok(schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(n));
    }
    @GetMapping("/averagegrade")
    public ResponseEntity<String> getAverageGrade(){
        return ResponseEntity.ok(schoolService.calculateAverageGrade());
    }
    @GetMapping("/top20percentscoringstudents")
    public ResponseEntity<List<Student>> getTopScoringStudents(){
        return ResponseEntity.ok(schoolService.getTopScoringStudents());
    }
}
