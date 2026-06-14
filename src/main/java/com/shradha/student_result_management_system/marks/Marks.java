package com.shradha.student_result_management_system.marks;


import com.shradha.student_result_management_system.student.Student;
import com.shradha.student_result_management_system.subject.Subject;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "marks",
       uniqueConstraints = {

        @UniqueConstraint(
                name = "uq_marks_student_subject",
                columnNames = {"student_id", "subject_id"}
        )
       })
public class Marks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //fk->student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="student_id",
            nullable = false
    )
    private Student student;

  //fk->subject
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "subject_id",
            nullable = false
    )
    private Subject subject;

    @Column(
            name = "marks_obtained",
            nullable = false

            // DECIMAL(5,2) — supports values like 95.50, 100.00
            // Never use FLOAT/DOUBLE for marks — rounding errors
    )
    private Double marksObtained;


    @Column(
            name = "grade",
            length = 2
    )
    private String grade;

    @Column(name = "exam_date")
    private LocalDate examDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
