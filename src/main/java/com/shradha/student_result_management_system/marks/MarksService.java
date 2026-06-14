package com.shradha.student_result_management_system.marks;

import java.util.List;

public interface MarksService {

    MarksResponseDTO addMarks(MarksRequestDto marksRequestDto);

    MarksResponseDTO getMarksById(Long id);

    List<MarksResponseDTO> getMarksByStudentId(Long StudentId);

    List<MarksResponseDTO> getMarksBySubjectId(Long subjectId);

    MarksResponseDTO updateMarks(Long id, MarksRequestDto requestDTO);

    void deleteMarks(Long id);

}
