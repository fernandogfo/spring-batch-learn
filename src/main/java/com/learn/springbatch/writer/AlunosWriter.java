package com.learn.springbatch.writer;

import com.learn.springbatch.component.AlunoConsultaBatch;
import com.learn.springbatch.dto.AlunoDTO;
import com.learn.springbatch.dto.AlunosSistemaExternoDTO;
import com.learn.springbatch.dto.ChaveAlunoRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class AlunosWriter implements ItemWriter<AlunosSistemaExternoDTO> {

    private final AlunoConsultaBatch consultaBatch;

    @Override
    public void write(Chunk<? extends AlunosSistemaExternoDTO> chunk) {

        try {

            List<ChaveAlunoRecord> chaves = chunk.getItems().stream()
                    .map(i -> new ChaveAlunoRecord(i.getCpf(), i.getDataNascimento(), i.getNomeMae(), i.getMatricula()))
                    .toList();

            List<AlunoDTO> encontrados = consultaBatch.buscarPorLote(chaves);

            Map<String, AlunoDTO> mapa = encontrados.stream()
                    .collect(Collectors.toMap(AlunoDTO::toChaveUnica, Function.identity()));

            for (AlunosSistemaExternoDTO entrada : chunk.getItems()) {
                String chave = new ChaveAlunoRecord(
                        entrada.getCpf(), entrada.getDataNascimento(), entrada.getNomeMae(), entrada.getMatricula()
                ).toChaveUnica();

                AlunoDTO aluno = mapa.get(chave);
                if (aluno != null) {
                    System.out.printf("Processando aluno: %s (%s)%n", aluno.getNome(), aluno.getCpf());
                } else {
                    System.out.println("Aluno não encontrado: " + chave);
                }
            }

        } catch (Exception e) {

            System.err.println("Erro ao processar chunk: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
