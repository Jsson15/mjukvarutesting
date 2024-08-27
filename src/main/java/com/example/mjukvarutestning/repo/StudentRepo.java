package com.example.mjukvarutestning.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.mjukvarutestning.entities.Student;
@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {

    // A "self-made" method, either query method or a native query, has to be tested
    boolean existsStudentByEmail(String email);
}
