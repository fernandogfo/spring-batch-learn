package com.learn.springbatch.step;

import com.learn.springbatch.dto.AlunosSistemaExternoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ProcessaAlunoStep {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ItemReader<AlunosSistemaExternoDTO> reader;
    private final ItemWriter<AlunosSistemaExternoDTO> writer;

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // controla quantas threads simultâneas
        executor.setMaxPoolSize(2);// controla o máximo de threads
        executor.setQueueCapacity(10); // rejeita se passar do limite
        executor.setThreadNamePrefix("processa-aluno-thread-");

        // ThreadFactory personalizada com logs
        executor.setThreadFactory(r -> {
            Thread thread = new Thread(r);
            thread.setName("processa-aluno-thread-" + thread.getId());
            System.out.println("Thread criada: " + thread.getName());
            return thread;
        });

        executor.initialize();
        return executor;
    }

    @Bean
    public Step processaAlunosStep() {
        return new StepBuilder("processaAlunosStep", jobRepository)
                .<AlunosSistemaExternoDTO, AlunosSistemaExternoDTO>chunk(1, transactionManager)
                .reader(reader)
                //.writer(writer)
                .writer(items -> {
                    System.out.println(
                            "Thread: " + Thread.currentThread().getName() +
                                    " | Processando chunk de " + items.size() + " itens" + items.getItems().get(0).getNome()
                    );
                    writer.write(items);
                })
                .taskExecutor(taskExecutor())
                .build();
    }
}
