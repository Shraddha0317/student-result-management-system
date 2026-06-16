package com.shradha.student_result_management_system.csv;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CsvErrorDTO {

    private int    rowNumber;   // which row failed (1-based, excluding header)
    private String data;
    private String reason;
}
