package com.shradha.student_result_management_system.student;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    //create
    StudentResponseDTO createStudent(StudentRequestDTO requestDTO);


    //read one
    StudentResponseDTO getStudentById(long id);

    // READ ALL — paginated
    Page<StudentResponseDTO> getAllStudents(Pageable pageable);

    // UPDATE
    StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO);

    // DELETE
    void deleteStudent(Long id);

}
