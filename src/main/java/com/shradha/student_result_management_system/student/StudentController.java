package com.shradha.student_result_management_system.student;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/getAllStudent")
 public ResponseEntity<Page<StudentResponseDTO>> getAllStudent(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(defaultValue = "id") String sortBy,
         @RequestParam(defaultValue = "asc") String sortDir
    )
    {

        Sort sort =sortDir.equalsIgnoreCase("desc")?Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable= PageRequest.of(page,size,sort);
        Page<StudentResponseDTO> student=studentService.getAllStudents(pageable);
        return ResponseEntity.ok(student);


    }

    @GetMapping("/{id}")

    public ResponseEntity<StudentResponseDTO> getStudentBYId(@PathVariable Long id){
         StudentResponseDTO Response= studentService.getStudentById(id);
         return ResponseEntity.ok(Response);

    }

   @PutMapping("/update/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable Long id, @RequestBody @Valid StudentRequestDTO studentRequestDTO){


       StudentResponseDTO respone= studentService.updateStudent(id,studentRequestDTO);
       return ResponseEntity.ok(respone);

   }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();

    }
}
