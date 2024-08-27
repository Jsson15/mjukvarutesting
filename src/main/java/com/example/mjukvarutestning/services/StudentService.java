package com.example.mjukvarutestning.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.mjukvarutestning.entities.Student;
import com.example.mjukvarutestning.repo.StudentRepo;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepo studentRepo;

    @Autowired
    public StudentService(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    public Student addStudent(Student student){
        boolean emailAlreadyExists = studentRepo.existsStudentByEmail(student.getEmail());
        if(emailAlreadyExists){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email " + student.getEmail() + " already exists");
        }
        return studentRepo.save(student);
    }

    public List<Student> getAllStudents(){
        return studentRepo.findAll();
    }

    public void deleteStudent(int id){
        if(!studentRepo.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find and delete student by id " + id);
        }
        studentRepo.deleteById(id);
    }

    public Student updateStudent(Student student){
        if(!studentRepo.existsById(student.getId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find and update student by id " + student.getId());
        }
        return studentRepo.save(student);
    }

    public Student getStudentById(int id) {
        return studentRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find student by id " + id));
    }

    public Student setGradeForStudentById(int studentId, String gradeAsString) {
        double grade;
        try {
            grade = Double.parseDouble(gradeAsString);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Valid grades are 0.0 - 5.0");
        }
        if(grade < 0 || grade > 5)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Valid grades are 0.0 - 5.0");
        Student student = studentRepo.findById(studentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find and update grades for student by id " + studentId)
        );
        student.setJavaProgrammingGrade(grade);
        return studentRepo.save(student);
    }
}
