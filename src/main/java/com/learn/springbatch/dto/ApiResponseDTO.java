package com.learn.springbatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO {
    private int page;
    private int size;
    private int totalPages;
    private List<AlunosSistemaExternoDTO> data;
}
