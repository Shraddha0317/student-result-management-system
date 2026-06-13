package com.shradha.student_result_management_system.student;


import com.shradha.student_result_management_system.exception.DuplicateResourceException;
import com.shradha.student_result_management_system.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {

        // Business rule 1: email must be unique
        if (studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Student already exists with email: " + requestDTO.getEmail());
        }


        // Business rule 2: roll number must be unique
        if (studentRepository.ExistByRollNumber(requestDTO.getRollNumber())) {
            throw new DuplicateResourceException("Student already exists with RollNumber: " + requestDTO.getEmail());
        }


        // Map DTO → Entity
        Student student = studentMapper.toEntity(requestDTO);

        // save to db
        Student saveStudent = studentRepository.save(student);
        //entity->reponse
        return studentMapper.toresponseDTO(saveStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("student not found with" + id));
        return studentMapper.toresponseDTO(student);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> getAllStudents(Pageable pageable) {

        return studentRepository.findAll(pageable).map(studentMapper::toresponseDTO);
    }


    @Override
    @Transactional
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO) {

        Student existingStudent = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("StudentNot Found With id" + id));

        if (studentRepository.existsByEmailAndIdNot(requestDTO.getEmail(), id)) {
            throw new DuplicateResourceException("Email already in use by another student: "
                    + requestDTO.getEmail());
        }


        if (studentRepository.existsByRollNumberAndIdNot(
                requestDTO.getRollNumber(), id)) {
            throw new DuplicateResourceException(
                    "Roll number already in use: " + requestDTO.getRollNumber()
            );
        }
            studentMapper.updateEntityFromDTO(requestDTO, existingStudent);
            Student updatedStudent = studentRepository.save(existingStudent);
             return studentMapper.toresponseDTO(updatedStudent);
        }


        @Override
        @Transactional
        public void deleteStudent (Long id){

            // Verify student exists before attempting delete
            // Why? deleteById() silently does nothing if id doesn't exist.
            // We want to return 404 if student not found.
            if (!studentRepository.existsById(id)) {
                throw new ResourceNotFoundException(
                        "Student not found with id: " + id
                );
            }

            studentRepository.deleteById(id);
            // If student has marks → DB throws FK constraint violation
            // GlobalExceptionHandler catches DataIntegrityViolationException
            // Returns 409 Conflict with meaningful message
        }

}