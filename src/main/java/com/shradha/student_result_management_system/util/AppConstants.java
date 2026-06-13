package com.shradha.student_result_management_system.util;

public class AppConstants {
    private AppConstants() {

    }

        public static final int MAX_MARKS = 100;
        public static final int PASS_MARKS = 35;

        public static final String PASS = "Pass";
        public static final String FAIL = "Fail";

    // Grade boundaries (percentage)
    public static final double GRADE_A_PLUS = 90.0;
    public static final double GRADE_A      = 80.0;
    public static final double GRADE_B      = 70.0;
    public static final double GRADE_C      = 60.0;
    public static final double GRADE_D      = 50.0;

    public static final double PASS_PERCENTAGE = 40.0;

    // Pagination defaults
    public static final int    DEFAULT_PAGE_NUMBER = 0;
    public static final int    DEFAULT_PAGE_SIZE   = 10;
    public static final String DEFAULT_SORT_BY     = "id";
    public static final String DEFAULT_SORT_DIR    = "asc";
}
