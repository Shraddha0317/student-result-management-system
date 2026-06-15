package com.shradha.student_result_management_system.result;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponseDto {

    // Student info
    private Long   studentId;
    private String studentName;
    private String rollNumber;

    // Per-subject breakdown
    private List<SubjectResultDto> subjects;

    // Aggregates — calculated by service
    private Double  totalMarksObtained;
    private Integer totalMaxMarks;
    private Double  overallPercentage;
    private String  overallGrade;

    // Final verdict
    private String result;          // "PASS" or "FAIL"
    private Integer totalSubjects;
}
