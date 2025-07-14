package com.learn.springbatch.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlunosSistemaExternoDTO {

    private String cpf;
    private String nomeMae;
    private String matricula;
    private LocalDate dataNascimento;
    private String nome;
    private boolean endMarker = false;

    public AlunosSistemaExternoDTO(boolean endMarker) {
        this.endMarker = endMarker;
    }
}
