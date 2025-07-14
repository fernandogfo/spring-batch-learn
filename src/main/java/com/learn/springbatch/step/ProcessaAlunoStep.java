package com.learn.springbatch.step;

import com.learn.springbatch.client.ExternalApiClient;
import com.learn.springbatch.component.AlunoConsultaBatch;
import com.learn.springbatch.dto.AlunosSistemaExternoDTO;
import com.learn.springbatch.reader.PagedAlunoItemReader;
import com.learn.springbatch.writer.AlunosWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class ProcessaAlunoStep {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AlunoConsultaBatch consultaBatch;
    private final ExternalApiClient externalApiClient;

    @Bean("asyncWriterAlunoExecutor")
    public TaskExecutor writerTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(6);
        executor.setThreadNamePrefix("processa-aluno-thread-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public Step processaAlunosStep(ItemReader<AlunosSistemaExternoDTO> pagedAlunoReader,
                                   ItemWriter<AlunosSistemaExternoDTO> alunosWriter) {
        return new StepBuilder("processaAlunosStep", jobRepository)
                .<AlunosSistemaExternoDTO, AlunosSistemaExternoDTO>chunk(10, transactionManager) // Aumente o chunk size
                .reader(pagedAlunoReader)
                .writer(items -> {
                    System.out.println(Thread.currentThread().getName() +
                            " | Processando " + items.size() + " itens");
                    alunosWriter.write(items);
                })
                .taskExecutor(writerTaskExecutor())
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(100)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<AlunosSistemaExternoDTO> pagedAlunoReader(
            ExternalApiClient apiClient,
            @Value("#{jobParameters['pageSize']}") Integer pageSize) {
        return new PagedAlunoItemReader(apiClient, pageSize != null ? pageSize : 2);
    }

    @Bean
    public ItemWriter<AlunosSistemaExternoDTO> alunosWriter() {
        return new AlunosWriter(consultaBatch);
    }
}