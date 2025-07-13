package com.learn.springbatch.component;

import com.learn.springbatch.dto.AlunoDTO;
import com.learn.springbatch.dto.ChaveAlunoRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class AlunoConsultaBatch {

    private final JdbcTemplate jdbcTemplate;

    public AlunoConsultaBatch(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AlunoDTO> buscarPorLote(List<ChaveAlunoRecord> chaves) {
        if (chaves.isEmpty()) {
            return List.of();
        }

        StringBuilder sql = new StringBuilder("""
                    SELECT cpf, data_nascimento, nome_mae, matricula, nome
                    FROM aluno
                    WHERE (cpf, data_nascimento, nome_mae, matricula) IN (
                """);

        List<Object> params = new ArrayList<>();
        for (int i = 0; i < chaves.size(); i++) {
            sql.append("(?, ?, ?, ?)");

            if (i < chaves.size() - 1) {
                sql.append(", ");
            }
            ChaveAlunoRecord c = chaves.get(i);
            params.add(c.cpf());
            params.add(c.dataNascimento());
            params.add(c.nomeMae());
            params.add(c.matricula());

        }

        sql.append(")");

        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) ->
                new AlunoDTO(
                        rs.getString("cpf"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getString("nome_mae"),
                        rs.getString("matricula"),
                        rs.getString("nome")
                )
        );
    }
}

