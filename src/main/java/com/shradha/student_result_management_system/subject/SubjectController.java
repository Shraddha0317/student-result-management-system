package com.shradha.student_result_management_system.subject;


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
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping("/add")
     public ResponseEntity<SubjectResponseDto> addSubject(@Valid @RequestBody SubjectRequestDto dto){
            SubjectResponseDto response =   subjectService.AddSubject(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<SubjectResponseDto>> getAllSubject(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    )
    {

        Sort sort =sortDir.equalsIgnoreCase("desc")?Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable= PageRequest.of(page,size,sort);
        Page<SubjectResponseDto> subject=subjectService.getAllSubject(pageable);
        return ResponseEntity.ok(subject);


    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDto> getById(@PathVariable Long id){
      SubjectResponseDto response=  subjectService.getSubjectById(id);
      return ResponseEntity.ok(response);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SubjectResponseDto> updateSubject(@PathVariable Long id, @RequestBody @Valid SubjectRequestDto subjectRequestDto){


     SubjectResponseDto respone= subjectService.updateSubject(subjectRequestDto, id);
        return ResponseEntity.ok(respone);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {

        subjectService.deletrSubject(id);
        return ResponseEntity.noContent().build();

    }


}
