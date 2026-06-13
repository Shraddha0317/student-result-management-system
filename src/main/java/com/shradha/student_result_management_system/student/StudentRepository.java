package com.shradha.student_result_management_system.student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {


    Optional<Student> findByEmail(String Email);

    boolean ExistByRollNumber(String rollNumber);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByRollNumberAndIdNot(String rollNumber, Long id);

    Optional<Student> findByRollNumber(String rollNumber);
}
