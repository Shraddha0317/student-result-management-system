package com.shradha.student_result_management_system.marks;


import com.shradha.student_result_management_system.util.GradeCalculator;
import org.springframework.stereotype.Component;

@Component

public class MarksMapper {

    public MarksResponseDTO toResponseDto(Marks marks){

        double Percentage = GradeCalculator.calculatePercentage(marks.getMarksObtained(),marks.getSubject().getMaxMarks());

        return MarksResponseDTO.builder()
                .id(marks.getId())
                .studentId(marks.getStudent().getId())
                .studentName(marks.getStudent().getFirstName()+" "+marks.getStudent().getLastName())
                .rollNumber(marks.getStudent().getRollNumber())
                .subjectId(marks.getSubject().getId())
                .subjectName(marks.getSubject().getSubjectName())
                .subjectCode(marks.getSubject().getSubjectCode())
                .maxMarks(marks.getSubject().getMaxMarks())
                .marksObtained(marks.getMarksObtained())
                .percentage(Percentage)
                .grade(marks.getGrade())
                .examDate(marks.getExamDate())
                .createdAt(marks.getCreatedAt())

                .build();
    }
}
