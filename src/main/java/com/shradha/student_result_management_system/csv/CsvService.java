package com.shradha.student_result_management_system.csv;

import org.springframework.web.multipart.MultipartFile;

public interface CsvService {



    CsvUploadResponseDTO uploadStudents(MultipartFile file);

    CsvUploadResponseDTO uploadMarks(MultipartFile file);
}
