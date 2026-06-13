//package com.shradha.student_result_management_system.utilTest;
//
//import com.shradha.student_result_management_system.util.GradeCalculator;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class GradeCalculatorTest {
//
//    @Test
//    void shouldReturnAPlus_whenPercentageIs90OrAbove( ){
//     GradeCalculator.calculateGrade(85);
//        assertEquals("A", GradeCalculator.calculateGrade(85));
//    }
//
//    @Test
//    void shouldReturnF_whenPercentageIsBelow35(){
//     String grade=   GradeCalculator.calculateGrade(34);
//        assertEquals("F",grade);
//    }
//
//
//    @Test
//    void shouldReturnPass_whenPercentageIs35OrAbove() {
//        String result = GradeCalculator.calculateResult(50);
//        assertEquals("Pass", result);
//    }
//
//
//    @Test
//    void shouldReturnFail_whenPercentageIsBelow35(){
//        String result = GradeCalculator.calculateResult(20);
//        assertEquals("Fail",result);
//    }
//
//    @Test
//    void shouldReturn0_whenTotalMarksIsZero(){
//        double percentage= GradeCalculator.calculatePercentage(50,0);
//        assertEquals(0,percentage);
//    }
//    @Test
//    void shouldCalculatePercentageCorrectly() {
//        double percentage = GradeCalculator.calculatePercentage(75, 100);
//        assertEquals(75.0, percentage);
//    }
//}
