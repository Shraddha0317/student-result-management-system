package com.shradha.student_result_management_system.subject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject,Long> {


    Optional<Subject> findBySubjectCode(String subCode);


    boolean existsBySubjectCode(String subCode);
    boolean existsBySubjectName(String Name);


    Optional<Subject> findBySubjectName(String name);





}
