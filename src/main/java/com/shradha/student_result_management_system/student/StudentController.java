package com.shradha.student_result_management_system.student;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;


    @PostMapping("/create")
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO requestDTO){

        StudentResponseDTO response = studentService.createStudent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    @GetMapping("/getAllStudent")
// public ResponseEntity<Page<StudentResponseDTO>>g
}
