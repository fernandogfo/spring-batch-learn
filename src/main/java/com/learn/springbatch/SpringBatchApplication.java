package com.learn.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBatchApplication implements CommandLineRunner {


    private final JobLauncher jobLauncher;
    private final Job processaAlunosJob;

    public SpringBatchApplication(JobLauncher jobLauncher, Job processaAlunosJob) {
        this.jobLauncher = jobLauncher;
        this.processaAlunosJob = processaAlunosJob;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()) // para ser sempre único
                .toJobParameters();

        jobLauncher.run(processaAlunosJob, jobParameters);
    }
}
