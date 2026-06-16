package com.shradha.student_result_management_system.csv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/csv")
@RequiredArgsConstructor
public class CsvController {

    private final CsvService csvService;

    @PostMapping("/students")
    public ResponseEntity<CsvUploadResponseDTO> uploadStudents(
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(csvService.uploadStudents(file));
    }

    @PostMapping("/marks")
    public ResponseEntity<CsvUploadResponseDTO> uploadMarks(
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(csvService.uploadMarks(file));
    }
}