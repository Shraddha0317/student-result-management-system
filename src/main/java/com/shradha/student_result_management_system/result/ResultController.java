package com.shradha.student_result_management_system.result;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    // GET /api/v1/results/student/{studentId}
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ResultResponseDto> getResultByStudent(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                resultService.getResultByStudentId(studentId)
        );
    }

    // GET /api/v1/results
    @GetMapping
    public ResponseEntity<List<ResultResponseDto>> getAllResults() {

        return ResponseEntity.ok(resultService.getAllResults());
    }
}