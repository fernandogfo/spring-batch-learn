package com.learn.springbatch.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learn.springbatch.dto.AlunosSistemaExternoDTO;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.SynchronizedItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class AlunosReader {

    @Bean
    public ItemReader<AlunosSistemaExternoDTO> alunoReader() {
        List<AlunosSistemaExternoDTO> alunos = carregarAlunosDoJson();
        ListItemReader<AlunosSistemaExternoDTO> delegateReader = new ListItemReader<>(alunos);
        return new SynchronizedItemReader<>(delegateReader); // 👈 garantimos thread-safe
    }

    public List<AlunosSistemaExternoDTO> carregarAlunosDoJson() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            ClassPathResource resource = new ClassPathResource("alunos/alunosExternos.json");
            InputStream inputStream = resource.getInputStream();

            return mapper.readValue(inputStream, new TypeReference<List<AlunosSistemaExternoDTO>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar dados dos alunos", e);
        }
    }
}
