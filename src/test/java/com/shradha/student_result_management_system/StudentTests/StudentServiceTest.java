package com.shradha.student_result_management_system.StudentTests;


import com.shradha.student_result_management_system.exception.DuplicateResourceException;
import com.shradha.student_result_management_system.exception.ResourceNotFoundException;
import com.shradha.student_result_management_system.student.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
   private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
   private StudentServiceImpl studentService;

    @Captor
    private ArgumentCaptor<Student> studentCaptor;


    private StudentRequestDTO requestDTO;
    private Student student;
    private StudentResponseDTO responseDTO;


     @BeforeEach
    void setUp(){
        requestDTO=StudentRequestDTO.builder()
                .firstName("Raj")
                .lastName("Kumar")
                .email("raj@gmail.com")
                .rollNumber("CS001")
                .dateOfBirth(LocalDate.of(2000, 5, 15))
                .build();


        student = Student.builder()
                .id(1L)
                .firstName("Raj")
                .lastName("Kumar")
                .email("raj@gmail.com")
                .rollNumber("CS001")
                .dateOfBirth(LocalDate.of(2000, 5, 15))
                .build();

        responseDTO = StudentResponseDTO.builder()
                .id(1L)
                .firstName("Raj")
                .lastName("Kumar")
                .fullName("Raj Kumar")
                .email("raj@gmail.com")
                .rollNumber("CS001")
                .build();

    }


    //create StudentTest


    @Test
    @DisplayName("createStudent: should save and return DTO when valid")
    void createStudent_shouldSaveAndReturnDTO_whenInputIsValid(){

        when(studentRepository.existsByEmail(requestDTO.getEmail()))
                .thenReturn(false);

        when(studentRepository.existsByRollNumber(requestDTO.getRollNumber())).thenReturn(false);

        when(studentMapper.toEntity(requestDTO))
                .thenReturn(student);

        when(studentRepository.save(student))
                .thenReturn(student);

        when(studentMapper.toresponseDTO(student))
                .thenReturn(responseDTO);


        StudentResponseDTO result = studentService.createStudent(requestDTO);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Raj", result.getFirstName());
        assertEquals("Raj Kumar",result.getFullName());
        assertEquals("raj@gmail.com", result.getEmail());

        verify(studentRepository, times(1)).save(any(Student.class));
    }



    @Test
    @DisplayName("createStudent: should throw 409 when email already exists")
    void createStudent_shouldThrowDuplicate_whenEmailAlreadyExists() {

        // ARRANGE
        // Mock: email already exists in DB
        when(studentRepository.existsByEmail(requestDTO.getEmail()))
                .thenReturn(true);

        // ACT + ASSERT
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> studentService.createStudent(requestDTO)
        );

        // Verify exception message is meaningful
        assertTrue(exception.getMessage()
                .contains(requestDTO.getEmail()));

        // CRITICAL: verify repository.save() was NEVER called
        // We must not save anything when email is duplicate
        verify(studentRepository, never()).save(any());
    }

    @Test
    @DisplayName("createStudent: should throw 409 when roll number already exists")
    void createStudent_shouldThrowDuplicate_whenRollNumberAlreadyExists() {

        // ARRANGE
        when(studentRepository.existsByEmail(requestDTO.getEmail()))
                .thenReturn(false);
        when(studentRepository.existsByRollNumber(requestDTO.getRollNumber()))
                .thenReturn(true);

        // ACT + ASSERT
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> studentService.createStudent(requestDTO)
        );

        assertTrue(exception.getMessage()
                .contains(requestDTO.getRollNumber()));
        verify(studentRepository, never()).save(any());
    }

    // ══════════════════════════════════════════════════
    // GET STUDENT BY ID TESTS
    // ══════════════════════════════════════════════════

    @Test
    @DisplayName("getStudentById: should return DTO when student exists")
    void getStudentById_shouldReturnDTO_whenStudentExists() {

        // ARRANGE
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(studentMapper.toresponseDTO(student))
                .thenReturn(responseDTO);

        // ACT
        StudentResponseDTO result = studentService.getStudentById(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("raj@gmail.com", result.getEmail());

        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getStudentById: should throw 404 when student not found")
    void getStudentById_shouldThrow404_whenStudentNotFound() {

        // ARRANGE
        // Mock: findById returns empty Optional (student doesn't exist)
        when(studentRepository.findById(999L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> studentService.getStudentById(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
    }

    // ══════════════════════════════════════════════════
    // GET ALL STUDENTS TESTS
    // ══════════════════════════════════════════════════

    @Test
    @DisplayName("getAllStudents: should return page of DTOs")
    void getAllStudents_shouldReturnPageOfDTOs() {

        // ARRANGE
        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> studentPage = new PageImpl<>(List.of(student));

        when(studentRepository.findAll(pageable))
                .thenReturn(studentPage);
        when(studentMapper.toresponseDTO(student))
                .thenReturn(responseDTO);

        // ACT
        Page<StudentResponseDTO> result =
                studentService.getAllStudents(pageable);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("Raj", result.getContent().get(0).getFirstName());
    }

    // ══════════════════════════════════════════════════
    // UPDATE STUDENT TESTS
    // ══════════════════════════════════════════════════

    @Test
    @DisplayName("updateStudent: should update and return DTO when valid")
    void updateStudent_shouldUpdateAndReturn_whenInputIsValid() {

        // ARRANGE
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(studentRepository.existsByEmailAndIdNot(
                requestDTO.getEmail(), 1L))
                .thenReturn(false);
        when(studentRepository.existsByRollNumberAndIdNot(
                requestDTO.getRollNumber(), 1L))
                .thenReturn(false);
        when(studentRepository.save(student))
                .thenReturn(student);
        when(studentMapper.toresponseDTO(student))
                .thenReturn(responseDTO);

        // ACT
        StudentResponseDTO result =
                studentService.updateStudent(1L, requestDTO);

        // ASSERT
        assertNotNull(result);

        // Verify mapper.updateEntityFromDTO was called with correct args
        verify(studentMapper, times(1))
                .updateEntityFromDTO(requestDTO, student);

        // Verify save was called
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    @DisplayName("updateStudent: should throw 404 when student not found")
    void updateStudent_shouldThrow404_whenStudentNotFound() {

        // ARRANGE
        when(studentRepository.findById(999L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                ResourceNotFoundException.class,
                () -> studentService.updateStudent(999L, requestDTO)
        );

        // save must never be called if student not found
        verify(studentRepository, never()).save(any());
    }

    // ══════════════════════════════════════════════════
    // DELETE STUDENT TESTS
    // ══════════════════════════════════════════════════

    @Test
    @DisplayName("deleteStudent: should delete when student exists")
    void deleteStudent_shouldDelete_whenStudentExists() {

        // ARRANGE
        when(studentRepository.existsById(1L)).thenReturn(true);

        // ACT
        studentService.deleteStudent(1L);

        // ASSERT — verify deleteById was called with correct id
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteStudent: should throw 404 when student not found")
    void deleteStudent_shouldThrow404_whenStudentNotFound() {

        // ARRANGE
        when(studentRepository.existsById(999L)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(
                ResourceNotFoundException.class,
                () -> studentService.deleteStudent(999L)
        );

        // deleteById must never be called if student doesn't exist
        verify(studentRepository, never()).deleteById(any());

}}
