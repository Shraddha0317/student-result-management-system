package com.shradha.student_result_management_system.result;

import com.shradha.student_result_management_system.exception.ResourceNotFoundException;
import com.shradha.student_result_management_system.marks.Marks;
import com.shradha.student_result_management_system.marks.MarksRepository;
import com.shradha.student_result_management_system.student.Student;
import com.shradha.student_result_management_system.student.StudentRepository;
import com.shradha.student_result_management_system.util.AppConstants;
import com.shradha.student_result_management_system.util.GradeCalculator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements  ResultService{


    private final StudentRepository studentRepository;
    private final MarksRepository marksRepository;


    @Override
    @Transactional
    public ResultResponseDto getResultByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()->new ResourceNotFoundException("Student not found with this id "+ studentId));

        //fetch all marks
      List<Marks> marksList =marksRepository.findByStudentId(studentId);

      if(marksList.isEmpty()){

          return ResultResponseDto.builder()
                  .studentId(student.getId())
                  .studentName(student.getFirstName()+" "+student.getLastName())
                  .rollNumber(student.getRollNumber())
                  .subjects(List.of())
                  .totalMarksObtained(0.0)
                  .totalMaxMarks(0)
                  .overallPercentage(0.0)
                  .overallGrade("N/A")
                  .result("N/A")
                  .totalSubjects(0)
                  .build();
      }


      List<SubjectResultDto> subjectResult =marksList.stream().map(
              marks->{
                  double percentage = GradeCalculator.calculatePercentage(marks.getMarksObtained(),marks.getSubject().getMaxMarks());

                  return SubjectResultDto.builder()
                          .subjectId(marks.getSubject().getId())
                          .subjectCode(marks.getSubject().getSubjectCode())
                          .subjectName(marks.getSubject().getSubjectName())
                          .marksObtained(marks.getMarksObtained())
                          .maxMarks(marks.getSubject().getMaxMarks())
                          .percentage(percentage)
                          .grade(marks.getGrade())
                          .build();

              }
      )
              .collect(Collectors.toList());

      double totalMarksObtained=marksList.stream().mapToDouble(Marks::getMarksObtained).sum();


        int totalMaxMarks = marksList.stream()
                .mapToInt(m -> m.getSubject().getMaxMarks())
                .sum();

        double overallPercentage = GradeCalculator.calculatePercentage(
                totalMarksObtained,
                totalMaxMarks
        );


        // Step 6: calculate overall grade
        String overallGrade = GradeCalculator.calculateGrade(
                totalMarksObtained,
                totalMaxMarks
        );


        boolean passedAllSubjects = subjectResult.stream()
                .allMatch(s -> s.getPercentage() >= AppConstants.PASS_PERCENTAGE);

        String result = (overallPercentage >= AppConstants.PASS_PERCENTAGE
                && passedAllSubjects)
                ? AppConstants.RESULT_PASS
                : AppConstants.RESULT_FAIL;



        return ResultResponseDto.builder()
                .studentId(student.getId())
                .studentName(student.getFirstName()
                        + " " + student.getLastName())
                .rollNumber(student.getRollNumber())
                .subjects(subjectResult)
                .totalMarksObtained(totalMarksObtained)
                .totalMaxMarks(totalMaxMarks)
                .overallPercentage(overallPercentage)
                .overallGrade(overallGrade)
                .result(result)
                .totalSubjects(marksList.size())
                .build();

    }






    @Override
    @Transactional(readOnly = true)
    public List<ResultResponseDto> getAllResults() {
        return studentRepository.findAll()
                .stream()
                .map(student -> getResultByStudentId(student.getId()))
                .collect(Collectors.toList());
    }
}
