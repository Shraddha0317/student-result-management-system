package com.shradha.student_result_management_system.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;        // derived: firstName + " " + lastName
                                    // set by Mapper — client convenience
    private String rollNumber;
    private String email;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
}
