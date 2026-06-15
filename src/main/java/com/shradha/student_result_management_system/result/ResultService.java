package com.shradha.student_result_management_system.result;

import java.util.List;

public interface ResultService {


    // Get full result for one student
    ResultResponseDto getResultByStudentId(Long studentId);

    // Get summary results for all students
    List<ResultResponseDto> getAllResults();
}
