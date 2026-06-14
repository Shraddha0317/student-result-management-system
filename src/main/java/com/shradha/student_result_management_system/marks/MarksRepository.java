package com.shradha.student_result_management_system.marks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends JpaRepository<Marks,Long> {

    List<Marks> findByStudentId(Long studentId);

    List<Marks> findBySubjectId(Long subjectId);

    boolean existsByStudentIdAndSubjectId(Long studentId, Long subjectId);

    Optional<Marks> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
}
