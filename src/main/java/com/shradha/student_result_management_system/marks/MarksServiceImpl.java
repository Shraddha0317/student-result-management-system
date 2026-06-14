package com.shradha.student_result_management_system.marks;


import com.shradha.student_result_management_system.exception.DuplicateResourceException;
import com.shradha.student_result_management_system.exception.ResourceNotFoundException;
import com.shradha.student_result_management_system.student.Student;
import com.shradha.student_result_management_system.student.StudentRepository;
import com.shradha.student_result_management_system.subject.Subject;
import com.shradha.student_result_management_system.subject.SubjectRepository;
import com.shradha.student_result_management_system.util.GradeCalculator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MarksServiceImpl implements MarksService {

    private final MarksRepository marksRepository;
    private  final StudentRepository studentRepository;
    private  final SubjectRepository subjectRepository;
    private final MarksMapper marksMapper;

    @Override
    @Transactional
    public MarksResponseDTO addMarks(MarksRequestDto marksRequestDto) {
        //checking for student existence
        Student student = studentRepository.findById(marksRequestDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + marksRequestDto.getStudentId()));

        // checking for subject existence

        Subject subject = subjectRepository.findById(marksRequestDto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + marksRequestDto.getSubjectId()));


        // One student can only have ONE marks entry per subject
        if (marksRepository.existsByStudentIdAndSubjectId(marksRequestDto.getStudentId(), marksRequestDto.getSubjectId())) {
            throw new DuplicateResourceException("Marks already exist for student id: "
                    + marksRequestDto.getStudentId()
                    + " and subject id: "
                    + marksRequestDto.getSubjectId());
        }


        // marksObtained cannot exceed subject's maxMarks
        // We can ONLY do this here in the service — not with annotations
        // because maxMarks comes from a different entity
        if (marksRequestDto.getMarksObtained() > subject.getMaxMarks()) {
            throw new IllegalArgumentException(
                    "Marks obtained (" + marksRequestDto.getMarksObtained()
                            + ") cannot exceed max marks ("
                            + subject.getMaxMarks() + ") for subject: "
                            + subject.getSubjectName()
            );


        }

        //  calculate grade
        String grade = GradeCalculator.calculateGrade(
                marksRequestDto.getMarksObtained(),
                subject.getMaxMarks()
        );

        //  build and save entity
        Marks marks = Marks.builder()
                .student(student)
                .subject(subject)
                .marksObtained(marksRequestDto.getMarksObtained())
                .grade(grade)
                .examDate(marksRequestDto.getExamDate())
                .build();

        Marks savedMarks = marksRepository.save(marks);
        return marksMapper.toResponseDto(savedMarks);

    }




    // GET BY ID
    @Override
    public MarksResponseDTO getMarksById(Long id) {
       Marks mark=marksRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Marks not found with id: " + id));

        return marksMapper.toResponseDto(mark);
    }




    // GET BY STUDENT
    @Override
    public List<MarksResponseDTO> getMarksByStudentId(Long StudentId) {

        if(!studentRepository.existsById(StudentId)){
            throw new ResourceNotFoundException("Student not found with id: " + StudentId);
        }


//        List<Marks> marksList =
//                marksRepository.findBYStudentId(StudentId);
//
//        List<MarksResponseDTO> responseList =
//                new ArrayList<>();
//
//        for(Marks mark : marksList) {
//
//            responseList.add(
//                    marksMapper.toResponseDto(mark)
//            );
//        }
//
//        return responseList;

        return marksRepository.findByStudentId(StudentId).stream().map(marksMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<MarksResponseDTO> getMarksBySubjectId(Long subjectId) {
     if(!subjectRepository.existsById(subjectId)){
         throw new ResourceNotFoundException( "Subject not found with id: " + subjectId);
     }


        return marksRepository.findBySubjectId(subjectId).stream().map(marksMapper::toResponseDto).collect((Collectors.toList()));
    }

    @Override
    public MarksResponseDTO updateMarks(Long id, MarksRequestDto requestDTO) {

        Marks existingMarks = marksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Marks not found with id: " + id
                ));

        Subject subject = subjectRepository
                .findById(requestDTO.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subject not found with id: " + requestDTO.getSubjectId()
                ));

        if (requestDTO.getMarksObtained() > subject.getMaxMarks()) {
            throw new IllegalArgumentException(
                    "Marks obtained (" + requestDTO.getMarksObtained()
                            + ") cannot exceed max marks ("
                            + subject.getMaxMarks() + ")"
            );
        }


        String grade = GradeCalculator.calculateGrade(
                requestDTO.getMarksObtained(),
                subject.getMaxMarks()
        );
        existingMarks.setMarksObtained(requestDTO.getMarksObtained());
        existingMarks.setGrade(grade);
        existingMarks.setExamDate(requestDTO.getExamDate());

        Marks updatedMarks = marksRepository.save(existingMarks);
        return marksMapper.toResponseDto(updatedMarks);
    }

    @Override
    public void deleteMarks(Long id) {


        if (!marksRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Marks not found with id: " + id
            );
        }
        marksRepository.deleteById(id);
    }
    }

