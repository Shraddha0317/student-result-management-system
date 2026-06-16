package com.shradha.student_result_management_system.csv;

import com.shradha.student_result_management_system.marks.Marks;
import com.shradha.student_result_management_system.marks.MarksRepository;
import com.shradha.student_result_management_system.student.Student;
import com.shradha.student_result_management_system.student.StudentRepository;
import com.shradha.student_result_management_system.subject.Subject;
import com.shradha.student_result_management_system.subject.SubjectRepository;
import com.shradha.student_result_management_system.util.GradeCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService{

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final MarksRepository marksRepository;


    @Override
    public CsvUploadResponseDTO uploadStudents(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("CSV file is empty");
        }



        List<CsvErrorDTO> errors       = new ArrayList<>();
          int successCount = 0;
          int  rowNumber  = 0;

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                file.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                );

                CSVParser csvParser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim()
                        .parse(reader)
        ) {
            for (CSVRecord record : csvParser) {
                rowNumber++;
                String rawData = record.toString();

                try {
                    // Extract values from CSV row
                    String firstName   = record.get("firstName");
                    String lastName    = record.get("lastName");
                    String email       = record.get("email");
                    String rollNumber  = record.get("rollNumber");
                    String dobString   = record.get("dateOfBirth");

                    // Row-level validation
                    if (firstName.isBlank() || lastName.isBlank()
                            || email.isBlank() || rollNumber.isBlank()) {
                        errors.add(CsvErrorDTO.builder()
                                .rowNumber(rowNumber)
                                .data(rawData)
                                .reason("Required fields missing: " +
                                        "firstName, lastName, email, rollNumber")
                                .build());
                        continue; // skip to next row — don't stop the whole upload
                    }

                    // Business rule: check duplicates
                    if (studentRepository.existsByEmail(email)) {
                        errors.add(CsvErrorDTO.builder()
                                .rowNumber(rowNumber)
                                .data(rawData)
                                .reason("Email already exists: " + email)
                                .build());
                        continue;
                    }

                    if (studentRepository.existsByRollNumber(rollNumber)) {
                        errors.add(CsvErrorDTO.builder()
                                .rowNumber(rowNumber)
                                .data(rawData)
                                .reason("Roll number already exists: " + rollNumber)
                                .build());
                        continue;
                    }

                    // Parse date of birth (optional)
                    LocalDate dob = null;
                    if (dobString != null && !dobString.isBlank()) {
                        dob = LocalDate.parse(dobString);
                        // Throws DateTimeParseException if format is wrong
                        // Caught by the catch block below
                    }

                    // Build and save student
                    Student student = Student.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .email(email)
                            .rollNumber(rollNumber)
                            .dateOfBirth(dob)
                            .build();

                    studentRepository.save(student);
                    successCount++;
                    log.info("CSV: saved student row {} - {}", rowNumber, email);

                } catch (Exception e) {
                    // Catch any unexpected error for this row
                    // Log it and continue to next row
                    log.error("CSV: error on row {} - {}", rowNumber, e.getMessage());
                    errors.add(CsvErrorDTO.builder()
                            .rowNumber(rowNumber)
                            .data(rawData)
                            .reason("Unexpected error: " + e.getMessage())
                            .build());
                }
                // Note: NO @Transactional on this method
                // Each studentRepository.save() is its own transaction
                // So a failure on row 5 never rolls back row 1-4
            }

        } catch (Exception e) {
            // File-level error — can't even read the file
            throw new IllegalArgumentException(
                    "Failed to parse CSV file: " + e.getMessage()
            );
        }

        return CsvUploadResponseDTO.builder()
                .totalRows(rowNumber)
                .successCount(successCount)
                .failureCount(errors.size())
                .errors(errors)
                .build();
    }


    @Override
    public CsvUploadResponseDTO uploadMarks(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("CSV file is empty");
        }

        List<CsvErrorDTO> errors       = new ArrayList<>();
        int               successCount = 0;
        int               rowNumber    = 0;

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                file.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                );
                CSVParser csvParser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim()
                        .parse(reader)
        ) {
            for (CSVRecord record : csvParser) {
                rowNumber++;
                String rawData = record.toString();

                try {
                    String rollNumber  = record.get("rollNumber");
                    String subjectCode = record.get("subjectCode");
                    String marksStr    = record.get("marksObtained");
                    String examDateStr = record.get("examDate");

                    // Validate required fields
                    if (rollNumber.isBlank() || subjectCode.isBlank()
                            || marksStr.isBlank()) {
                        errors.add(CsvErrorDTO.builder()
                                .rowNumber(rowNumber)
                                .data(rawData)
                                .reason("Required fields missing: " +
                                        "rollNumber, subjectCode, marksObtained")
                                .build());
                        continue;
                    }

                    // Look up student by rollNumber
                    Optional<Student> studentOpt =
                            studentRepository.findByRollNumber(rollNumber);

                    if (studentOpt.isEmpty()) {
                        errors.add(CsvErrorDTO.builder()
                                .rowNumber(rowNumber)
                                .data(rawData)
                                .reason("Student not found with roll number: "
                                        + rollNumber)
                                .build());
                        continue;
                    }

                    // Look up subject by subjectCode
                    Optional<Subject> subjectOpt =
                            subjectRepository.findBySubjectCode(subjectCode);

                    if (subjectOpt.isEmpty()) {
                        errors.add(CsvErrorDTO.builder()
                                .rowNumber(rowNumber)
                                .data(rawData)
                                .reason("Subject not found with code: "
                                        + subjectCode)
                                .build());
                        continue;
                    }

                    Student student = studentOpt.get();
                    Subject subject = subjectOpt.get();

                    // Parse marks
                    double marksObtained = Double.parseDouble(marksStr);

                    // Validate marks range
                    if (marksObtained < 0) {
                        errors.add(CsvErrorDTO.builder()
                                .rowNumber(rowNumber)
                                .data(rawData)
                                .reason("Marks cannot be negative: "
                                        + marksObtained)
                                .build());
                        continue;
                    }

                    if (marksObtained > subject.getMaxMarks()) {
                        errors.add(CsvErrorDTO.builder()
                                .rowNumber(rowNumber)
                                .data(rawData)
                                .reason("Marks (" + marksObtained
                                        + ") exceed max marks ("
                                        + subject.getMaxMarks() + ")")
                                .build());
                        continue;
                    }

                    // Check duplicate
                    if (marksRepository.existsByStudentIdAndSubjectId(
                            student.getId(), subject.getId())) {
                        errors.add(CsvErrorDTO.builder()
                                .rowNumber(rowNumber)
                                .data(rawData)
                                .reason("Marks already exist for "
                                        + rollNumber + " in " + subjectCode)
                                .build());
                        continue;
                    }

                    // Parse exam date (optional)
                    LocalDate examDate = null;
                    if (examDateStr != null && !examDateStr.isBlank()) {
                        examDate = LocalDate.parse(examDateStr);
                    }

                    // Calculate grade
                    String grade = GradeCalculator.calculateGrade(
                            marksObtained,
                            subject.getMaxMarks()
                    );

                    // Build and save
                    Marks marks = Marks.builder()
                            .student(student)
                            .subject(subject)
                            .marksObtained(marksObtained)
                            .grade(grade)
                            .examDate(examDate)
                            .build();

                    marksRepository.save(marks);
                    successCount++;
                    log.info("CSV: saved marks row {} - {} {}",
                            rowNumber, rollNumber, subjectCode);

                } catch (NumberFormatException e) {
                    errors.add(CsvErrorDTO.builder()
                            .rowNumber(rowNumber)
                            .data(rawData)
                            .reason("Invalid number format for marksObtained")
                            .build());
                } catch (Exception e) {
                    log.error("CSV: error on row {} - {}", rowNumber, e.getMessage());
                    errors.add(CsvErrorDTO.builder()
                            .rowNumber(rowNumber)
                            .data(rawData)
                            .reason("Unexpected error: " + e.getMessage())
                            .build());
                }
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to parse CSV file: " + e.getMessage()
            );
        }

        return CsvUploadResponseDTO.builder()
                .totalRows(rowNumber)
                .successCount(successCount)
                .failureCount(errors.size())
                .errors(errors)
                .build();
    }
}
