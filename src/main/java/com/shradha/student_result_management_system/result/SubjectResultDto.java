package com.shradha.student_result_management_system.result;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectResultDto {

    private Long   subjectId;
    private String subjectCode;
    private String subjectName;
    private Double marksObtained;
    private Integer maxMarks;
    private Double percentage;
    private String grade;
}
