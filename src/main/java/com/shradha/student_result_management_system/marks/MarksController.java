package com.shradha.student_result_management_system.marks;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/marks")
@RequiredArgsConstructor
public class MarksController {

    private final MarksService marksService;

    @PostMapping("/addMarks")
    public ResponseEntity<MarksResponseDTO> addMarks(
            @Valid @RequestBody MarksRequestDto requestDTO) {

        MarksResponseDTO response = marksService.addMarks(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarksResponseDTO> getMarksById(
            @PathVariable Long id) {

        return ResponseEntity.ok(marksService.getMarksById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<MarksResponseDTO>> getMarksByStudent(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(marksService.getMarksByStudentId(studentId));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<MarksResponseDTO>> getMarksBySubject(
            @PathVariable Long subjectId) {

        return ResponseEntity.ok(marksService.getMarksBySubjectId(subjectId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarksResponseDTO> updateMarks(
            @PathVariable Long id,
            @Valid @RequestBody MarksRequestDto requestDTO) {

        return ResponseEntity.ok(marksService.updateMarks(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarks(@PathVariable Long id) {

        marksService.deleteMarks(id);
        return ResponseEntity.noContent().build();
    }
}