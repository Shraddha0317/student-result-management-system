package com.shradha.student_result_management_system.MarksTest;


import com.shradha.student_result_management_system.exception.DuplicateResourceException;
import com.shradha.student_result_management_system.exception.ResourceNotFoundException;
import com.shradha.student_result_management_system.marks.*;
import com.shradha.student_result_management_system.student.Student;
import com.shradha.student_result_management_system.student.StudentRepository;
import com.shradha.student_result_management_system.subject.Subject;
import com.shradha.student_result_management_system.subject.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarksServiceTest {

    @Mock private MarksRepository marksRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private SubjectRepository subjectRepository;
    @Mock private MarksMapper       marksMapper;

    @InjectMocks
    private MarksServiceImpl marksService;

    private MarksRequestDto requestDTO;
    private Student student;
    private Subject  subject;
    private Marks marks;
    private MarksResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        requestDTO = MarksRequestDto.builder()
                .studentId(1L)
                .subjectId(1L)
                .marksObtained(85.0)
                .build();

        student = Student.builder()
                .id(1L)
                .firstName("Raj")
                .lastName("Kumar")
                .rollNumber("CS001")
                .email("raj@gmail.com")
                .build();

        subject = Subject.builder()
                .id(1L)
                .subjectCode("SPRING101")
                .subjectName("Spring Boot")
                .maxMarks(100)
                .build();

        marks = Marks.builder()
                .id(1L)
                .student(student)
                .subject(subject)
                .marksObtained(85.0)
                .grade("A")
                .build();

        responseDTO = MarksResponseDTO.builder()
                .id(1L)
                .studentId(1L)
                .studentName("Raj Kumar")
                .subjectId(1L)
                .subjectName("Spring Boot")
                .marksObtained(85.0)
                .grade("A")
                .percentage(85.0)
                .build();
    }

    @Test
    @DisplayName("addMarks: should save marks when all inputs are valid")
    void addMarks_shouldSaveMarks_whenInputIsValid() {

        // ARRANGE
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));
        when(marksRepository.existsByStudentIdAndSubjectId(1L, 1L))
                .thenReturn(false);
        when(marksRepository.save(any(Marks.class)))
                .thenReturn(marks);
        when(marksMapper.toResponseDto(marks))
                .thenReturn(responseDTO);

        // ACT
        MarksResponseDTO result = marksService.addMarks(requestDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals(85.0, result.getMarksObtained());
        assertEquals("A",  result.getGrade());
        verify(marksRepository, times(1)).save(any(Marks.class));
    }

    @Test
    @DisplayName("addMarks: should throw 404 when student not found")
    void addMarks_shouldThrow404_whenStudentNotFound() {

        // ARRANGE
        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> marksService.addMarks(requestDTO)
        );

        assertTrue(ex.getMessage().contains("Student not found"));
        verify(marksRepository, never()).save(any());
    }

    @Test
    @DisplayName("addMarks: should throw 404 when subject not found")
    void addMarks_shouldThrow404_whenSubjectNotFound() {

        // ARRANGE
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                ResourceNotFoundException.class,
                () -> marksService.addMarks(requestDTO)
        );
        verify(marksRepository, never()).save(any());
    }

    @Test
    @DisplayName("addMarks: should throw 409 when marks already exist")
    void addMarks_shouldThrow409_whenDuplicateMarksEntry() {

        // ARRANGE
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));
        when(marksRepository.existsByStudentIdAndSubjectId(1L, 1L))
                .thenReturn(true);  // ← duplicate exists

        // ACT + ASSERT
        assertThrows(
                DuplicateResourceException.class,
                () -> marksService.addMarks(requestDTO)
        );
        verify(marksRepository, never()).save(any());
    }

    @Test
    @DisplayName("addMarks: should throw 400 when marks exceed maxMarks")
    void addMarks_shouldThrow400_whenMarksExceedMaxMarks() {

        // ARRANGE
        requestDTO.setMarksObtained(150.0); // exceeds maxMarks=100

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));
        when(marksRepository.existsByStudentIdAndSubjectId(1L, 1L))
                .thenReturn(false);

        // ACT + ASSERT
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> marksService.addMarks(requestDTO)
        );

        assertTrue(ex.getMessage().contains("cannot exceed max marks"));
        verify(marksRepository, never()).save(any());
    }

    @Test
    @DisplayName("getMarksById: should throw 404 when marks not found")
    void getMarksById_shouldThrow404_whenNotFound() {

        when(marksRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> marksService.getMarksById(999L)
        );
    }

    @Test
    @DisplayName("deleteMarks: should delete when marks exist")
    void deleteMarks_shouldDelete_whenMarksExist() {

        when(marksRepository.existsById(1L)).thenReturn(true);

        marksService.deleteMarks(1L);

        verify(marksRepository, times(1)).deleteById(1L);
    }
}