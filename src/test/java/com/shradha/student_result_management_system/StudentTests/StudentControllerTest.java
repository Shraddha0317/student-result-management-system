package com.shradha.student_result_management_system.StudentTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.shradha.student_result_management_system.exception.ResourceNotFoundException;
import com.shradha.student_result_management_system.student.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;





import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)

// @WebMvcTest: loads ONLY the web layer (controller + filters)
// No database, no full Spring context — fast
class  StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    // MockMvc: simulates HTTP requests without a real server

    @Autowired
    private ObjectMapper objectMapper;
    // ObjectMapper: converts objects to/from JSON

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private StudentMapper studentMapper;

    private StudentRequestDTO requestDTO;
    private StudentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        requestDTO = StudentRequestDTO.builder()
                .firstName("Raj")
                .lastName("Kumar")
                .email("raj@gmail.com")
                .rollNumber("CS001")
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

    @Test
    @DisplayName("POST /students: should return 201 when valid")
    void createStudent_shouldReturn201_whenValid() throws Exception {

        // ARRANGE
        when(studentService.createStudent(any(StudentRequestDTO.class)))
                .thenReturn(responseDTO);

        // ACT + ASSERT
        mockMvc.perform(
                        post("/api/v1/students/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isCreated())                    // 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Raj"))
                .andExpect(jsonPath("$.fullName").value("Raj Kumar"))
                .andExpect(jsonPath("$.email").value("raj@gmail.com"));
    }

    @Test
    @DisplayName("POST /students: should return 400 when firstName is blank")
    void createStudent_shouldReturn400_whenFirstNameIsBlank() throws Exception {

        // ARRANGE — blank firstName
        requestDTO.setFirstName("");

        // ACT + ASSERT
        mockMvc.perform(
                        post("/api/v1/students/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isBadRequest())                 // 400
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.firstName").exists());
    }

    @Test
    @DisplayName("POST /students: should return 400 when email is invalid")
    void createStudent_shouldReturn400_whenEmailIsInvalid() throws Exception {

        requestDTO.setEmail("not-an-email");

        mockMvc.perform(
                        post("/api/v1/students/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    @DisplayName("GET /students/{id}: should return 200 when found")
    void getStudentById_shouldReturn200_whenFound() throws Exception {

        when(studentService.getStudentById(1L))
                .thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("raj@gmail.com"));
    }

    @Test
    @DisplayName("GET /students/{id}: should return 404 when not found")
    void getStudentById_shouldReturn404_whenNotFound() throws Exception {

        when(studentService.getStudentById(999L))
                .thenThrow(new ResourceNotFoundException(
                        "Student not found with id: 999"
                ));

        mockMvc.perform(get("/api/v1/students/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Student not found with id: 999"));
    }

    @Test
    @DisplayName("GET /students: should return 200 with page")
    void getAllStudents_shouldReturn200_withPage() throws Exception {

        Page<StudentResponseDTO> page =
                new PageImpl<>(List.of(responseDTO));

        when(studentService.getAllStudents(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/students/getAllStudent")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("DELETE /students/{id}: should return 204")
    void deleteStudent_shouldReturn204() throws Exception {

        mockMvc.perform(delete("/api/v1/students/1"))
                .andExpect(status().isNoContent());
    }
}