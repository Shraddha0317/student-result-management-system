package com.shradha.student_result_management_system.utilTest;

import com.shradha.student_result_management_system.util.GradeCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class GradeCalculatorTest {


    // ── calculateGrade tests ─────────────────────────
    @Test
    @DisplayName("Should return A+ when percentage is 90 or above")
    void calculateGrade_shouldReturnAPlus_whenPercentageIs90OrAbove(){

        assertEquals("A+",GradeCalculator.calculateGrade(90.0,100));
        assertEquals("A+", GradeCalculator.calculateGrade(95.0, 100));
        assertEquals("A+", GradeCalculator.calculateGrade(100.0, 100));

    }

    @Test
    @DisplayName("Should return A when percentage is between 80 and 89")
    void calculateGrade_shouldReturnA_whenPercentageIsBetween80And89() {

        assertEquals("A", GradeCalculator.calculateGrade(80.0, 100));
        assertEquals("A", GradeCalculator.calculateGrade(85.0, 100));
        assertEquals("A", GradeCalculator.calculateGrade(89.9, 100));
    }

    @Test
    @DisplayName("Should return B when percentage is between 70 and 79")
    void calculateGrade_shouldReturnB_whenPercentageIsBetween70And79() {

        assertEquals("B", GradeCalculator.calculateGrade(70.0, 100));
        assertEquals("B", GradeCalculator.calculateGrade(75.0, 100));
    }

    @Test
    @DisplayName("Should return C when percentage is between 60 and 69")
    void calculateGrade_shouldReturnC_whenPercentageIsBetween60And69() {

        assertEquals("C", GradeCalculator.calculateGrade(60.0, 100));
        assertEquals("C", GradeCalculator.calculateGrade(65.0, 100));
    }

    @Test
    @DisplayName("Should return D when percentage is between 50 and 59")
    void calculateGrade_shouldReturnD_whenPercentageIsBetween50And59() {

        assertEquals("D", GradeCalculator.calculateGrade(50.0, 100));
        assertEquals("D", GradeCalculator.calculateGrade(55.0, 100));
    }

    @Test
    @DisplayName("Should return F when percentage is below 50")
    void calculateGrade_shouldReturnF_whenPercentageIsBelowPassMark() {

        assertEquals("F", GradeCalculator.calculateGrade(0.0,  100));
        assertEquals("F", GradeCalculator.calculateGrade(30.0, 100));
        assertEquals("F", GradeCalculator.calculateGrade(49.9, 100));
    }


    //Parameterized test — tests multiple inputs in one method

    @ParameterizedTest
    @DisplayName("Grade boundaries with different maxMarks")
    @CsvSource({
            "75.0, 80,  A+",   // 93.75% → A+
            "85.0, 100, A",    // 85%    → A
            "72.0, 100, B",    // 72%    → B
            "30.0, 100, F",    // 30%    → F
            "80.0, 150, D"     // 53.33% → D
    })

    void calculateGrade_withDifferentMaxMarks(
            double obtained, int maxMarks, String expectedGrade){

        assertEquals(expectedGrade,
                GradeCalculator.calculateGrade(obtained, maxMarks));
    }

    @Test
    @DisplayName("Should throw exception when maxMarks is zero")
    void calculateGrade_shouldThrowException_whenMaxMarksIsZero() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> GradeCalculator.calculateGrade(80.0, 0)
        );

        assertTrue(exception.getMessage()
                .contains("Max marks must be greater than zero"));
    }



    // ── calculatePercentage tests ────────────────────

    @Test
    @DisplayName("Should calculate percentage correctly")
    void calculatePercentage_shouldReturnCorrectValue() {

        assertEquals(85.0,  GradeCalculator.calculatePercentage(85.0,  100));
        assertEquals(93.75, GradeCalculator.calculatePercentage(75.0,  80));
        assertEquals(53.33, GradeCalculator.calculatePercentage(80.0,  150));
        assertEquals(0.0,   GradeCalculator.calculatePercentage(0.0,   100));
        assertEquals(100.0, GradeCalculator.calculatePercentage(100.0, 100));
    }

}
