package com.shradha.student_result_management_system.csv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CsvUploadResponseDTO {

    private int  totalRows;
    private int  successCount;
    private int  failureCount;
    private List<CsvErrorDTO> errors;
}