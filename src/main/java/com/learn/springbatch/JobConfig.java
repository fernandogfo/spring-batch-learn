package com.learn.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.CommandLineRunner;
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
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            JobLauncher jobLauncher,
            Job processaAlunosJob) {
        return args -> {
            JobParameters parameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .addLong("pageSize", 2L)
                    .toJobParameters();

            jobLauncher.run(processaAlunosJob, parameters);
        };
    }

}
