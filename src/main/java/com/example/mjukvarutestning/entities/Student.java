package com.example.mjukvarutestning.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(precision = 3, scale = 2)
    private Double javaProgrammingGrade;  // För betyg mellan 0.0 och 5.0

    // Standardkonstruktor
    public Student() {
    }

    // Konstruktor utan javaProgrammingGrade
    public Student(String firstName, String lastName, LocalDate birthDate, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
    }

    // Konstruktor som inkluderar javaProgrammingGrade
    public Student(String firstName, String lastName, LocalDate birthDate, String email, Double javaProgrammingGrade) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.javaProgrammingGrade = javaProgrammingGrade;
    }

    // Getters och Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getJavaProgrammingGrade() {
        return javaProgrammingGrade;
    }

    public void setJavaProgrammingGrade(Double javaProgrammingGrade) {
        this.javaProgrammingGrade = javaProgrammingGrade;
    }

    public int getAge() {
        LocalDate today = LocalDate.now();
        return Period.between(birthDate, today).getYears();
    }
}
