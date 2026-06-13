package com.shradha.student_result_management_system.subject;


import com.shradha.student_result_management_system.student.StudentResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class SubjectMapper {


  public Subject toEntity(SubjectRequestDto dto){
      return Subject.builder()
              .subjectCode(dto.getSubjectCode())
              .subjectName(dto.getSubjectName())
              .maxMarks(dto.getMaxMarks())
              .description(dto.getDescription())
              .build();
  }

  public SubjectResponseDto toResponseDTO(Subject subject){

     return SubjectResponseDto.builder()
             .id(subject.getId())
             .subjectCode(subject.getSubjectCode())
             .subjectName(subject.getSubjectName())
             .maxMarks(subject.getMaxMarks())
             .description(subject.getDescription())
             .createdAt(subject.getCreatedAt())
             .build();
  }

  public void updateEntityFromDTO(SubjectRequestDto dto, Subject subject){

      subject.setSubjectCode(dto.getSubjectCode());
      subject.setSubjectName(dto.getSubjectName());
      subject.setMaxMarks(dto.getMaxMarks());
      subject.setDescription(dto.getDescription());
  }


}
