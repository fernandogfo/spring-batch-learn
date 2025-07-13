package com.learn.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class JobConfig {

    private final JobRepository jobRepository;
    private final Step processaAlunosStep;

    @Bean
    public Job processaAlunosJob() {
        return new JobBuilder("processaAlunosJob", jobRepository)
                .start(processaAlunosStep)
                .build();
    }

}
