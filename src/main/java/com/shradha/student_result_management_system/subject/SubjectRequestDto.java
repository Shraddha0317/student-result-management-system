package com.shradha.student_result_management_system.subject;

//RequestDTO validations:
//  subjectCode   @NotBlank  @Size(max=20)
//  subjectName   @NotBlank  @Size(max=100)
//  maxMarks      @NotNull   @Min(1)        @Max(1000)
//  description   optional — no validation needed

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequestDto {

    @NotBlank(message = "subjectcode is required")
    @Size(max = 20, message = " subjectCode cannot exceed 50 characters")
    private String subjectCode;

    @NotBlank(message = "subject Name is required")
    @Size(max = 100, message = " subject Name cannot exceed 50 characters")
    private String subjectName;

    @NotNull(message = "maxMarks is required")
    @Min(value = 1, message = "maxMarks must be at least 1")
    @Max(value = 1000, message = "maxMarks cannot exceed 1000")
    private Integer maxMarks;

    @NotNull
    private  String description;
}
