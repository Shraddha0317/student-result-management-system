package com.shradha.student_result_management_system.subject;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectResponseDto {


    private Long  id;
    private String subjectCode;
    private String subjectName;
    private Integer maxMarks;
    private  String description;
    private LocalDateTime createdAt;

}
