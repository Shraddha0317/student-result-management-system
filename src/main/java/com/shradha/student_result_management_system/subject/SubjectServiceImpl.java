package com.shradha.student_result_management_system.subject;

import com.shradha.student_result_management_system.exception.DuplicateResourceException;
import com.shradha.student_result_management_system.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService{
     private  final SubjectRepository subjectRepository;
     private final SubjectMapper subjectMapper;


    @Override
    @Transactional
    public SubjectResponseDto AddSubject(SubjectRequestDto requestDto) {

        if(subjectRepository.existsBySubjectCode(requestDto.getSubjectCode())){
            throw  new DuplicateResourceException("Subject alrady prsent with this "+requestDto.getSubjectCode());
        }



        Subject subject = subjectMapper.toEntity(requestDto);

        Subject saved=   subjectRepository.save(subject);

        return subjectMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectResponseDto getSubjectById(Long id) {
       Subject subject= subjectRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Subject not found with" + id));

        return subjectMapper.toResponseDTO(subject);
    }

    @Override
    public SubjectResponseDto getSubjectBySubjectCode(String code) {
      Subject subject=subjectRepository.findBySubjectCode(code).orElseThrow(()->
              new ResourceNotFoundException("Subject not found with code "+code));

        return subjectMapper.toResponseDTO(subject);
    }

    @Override
    public Page<SubjectResponseDto> getAllSubject(Pageable pagable) {

        return subjectRepository.findAll(pagable).map(subjectMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public SubjectResponseDto updateSubject(SubjectRequestDto requestDto, Long id) {

        Subject existingsub= subjectRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Subject not found with "+id));

        if(subjectRepository.existsBySubjectCode(requestDto.getSubjectCode())){
            throw new DuplicateResourceException("already present with this subject code"+requestDto.getSubjectCode());
        }

        subjectMapper.updateEntityFromDTO(requestDto,existingsub);
        Subject subject= subjectRepository.save(existingsub);

        return subjectMapper.toResponseDTO(subject);
    }

    @Override
    public void deletrSubject(Long id) {

        if(!subjectRepository.existsById(id)){
            throw new ResourceNotFoundException("subject not present with this id"+ id);
        }
        subjectRepository.deleteById(id);


    }
}
