package com.shradha.student_result_management_system.util;

public class GradeCalculator {

    // Private constructor — this is a utility class
    // Nobody should instantiate it: new GradeCalculator() makes no sense
    private GradeCalculator() {}

    public static String calculateGrade(double marksObtained, int maxMarks) {

        if (maxMarks <= 0) {
            throw new IllegalArgumentException(
                    "Max marks must be greater than zero"
            );
        }

        double percentage = (marksObtained / maxMarks) * 100;

        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        return "F";
    }

    public static double calculatePercentage(double marksObtained,
                                             int maxMarks) {
        if (maxMarks <= 0) {
            throw new IllegalArgumentException(
                    "Max marks must be greater than zero"
            );
        }
        // Round to 2 decimal places
        return Math.round((marksObtained / maxMarks) * 100.0 * 100.0) / 100.0;
    }
}