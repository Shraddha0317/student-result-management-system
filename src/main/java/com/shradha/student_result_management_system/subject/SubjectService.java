package com.shradha.student_result_management_system.subject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubjectService {


    SubjectResponseDto AddSubject(SubjectRequestDto requestDto);
    SubjectResponseDto getSubjectById(Long id);
    SubjectResponseDto getSubjectBySubjectCode(String code);

    Page<SubjectResponseDto> getAllSubject(Pageable pagable);

    SubjectResponseDto updateSubject(SubjectRequestDto requestDto,Long id);

    void deletrSubject(Long id);



}
