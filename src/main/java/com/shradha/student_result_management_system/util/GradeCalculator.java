package com.shradha.student_result_management_system.util;

public class GradeCalculator {

    private GradeCalculator(){}

    public static String calculateGrade(double percentage){
        if (percentage >= 90) return "A+";
        else if (percentage >= 75) return "A";
        else if (percentage >= 60) return "B";
        else if (percentage >= 50) return "C";
        else if (percentage >= 35) return "D";
        else return "F";
    }
    public static String calculateResult(double percentage){

        return percentage>=AppConstants.PASS_MARKS ? AppConstants.PASS :AppConstants.FAIL;
    }


    public static double calculatePercentage(double marksObtained, double totalMarks){
   if(totalMarks ==0){
       return 0;
   }else{
       return (marksObtained / totalMarks) * 100;
   }

    }
}
