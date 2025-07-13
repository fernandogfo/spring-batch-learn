package com.learn.springbatch.dto;

import java.time.LocalDate;

public class AlunoDTO {
    private String cpf;
    private LocalDate dataNascimento;
    private String nomeMae;
    private String matricula;
    private String nome;

    // Construtor
    public AlunoDTO(String cpf, LocalDate dataNascimento, String nomeMae,
                    String matricula, String nome) {
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.nomeMae = nomeMae;
        this.matricula = matricula;
        this.nome = nome;
    }

    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Método toString() para representação em string
    @Override
    public String toString() {
        return "AlunoDTO{" +
                "cpf='" + cpf + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", nomeMae='" + nomeMae + '\'' +
                ", matricula='" + matricula + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }

    public String toChaveUnica() {
        return cpf + "|" + dataNascimento + "|" + nomeMae + "|" + matricula;
    }
}
