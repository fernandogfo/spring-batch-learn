package com.learn.springbatch.dto;

import java.time.LocalDate;

public record ChaveAlunoRecord(String cpf, LocalDate dataNascimento, String nomeMae, String matricula) {
    public String toChaveUnica() {
        return cpf + "|" + dataNascimento + "|" + nomeMae + "|" + matricula;
    }
}
