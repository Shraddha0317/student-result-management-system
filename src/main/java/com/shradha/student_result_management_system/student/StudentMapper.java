package com.shradha.student_result_management_system.student;


import org.springframework.stereotype.Component;

@Component
public class StudentMapper {


    public Student toEntity(StudentRequestDTO dto){
        return Student.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .rollNumber(dto.getRollNumber())
                .email(dto.getEmail())
                .dateOfBirth(dto.getDateOfBirth())
                .build();
    }

    public StudentResponseDTO toresponseDTO(Student student){

        return StudentResponseDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .fullName(student.getFirstName()+" "+student.getLastName())
                .rollNumber(student.getRollNumber())
                .email(student.getEmail())
                .dateOfBirth(student.getDateOfBirth())
                .createdAt(student.getCreatedAt())
                .build();
    }

    public void updateEntityFromDTO(StudentRequestDTO dto, Student student) {

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setRollNumber(dto.getRollNumber());
        student.setEmail(dto.getEmail());
        student.setDateOfBirth(dto.getDateOfBirth());

    }
}
