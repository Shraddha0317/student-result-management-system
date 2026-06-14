// MarksResponseDTO.java
package com.shradha.student_result_management_system.marks;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarksResponseDTO {

    private Long id;

    // Student info —  from Student entity
    private Long studentId;
    private String studentName;     // firstName + lastName
    private String rollNumber;

    // Subject info —  from Subject entity
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private Integer maxMarks;

    // Marks data
    private Double marksObtained;
    private Double percentage;
    private String grade;           // calculated by GradeCalculator
    private LocalDate examDate;

    private LocalDateTime createdAt;
}