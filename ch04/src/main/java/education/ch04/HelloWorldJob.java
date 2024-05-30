package education.ch04;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class HelloWorldJob {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public Step step() {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Hello, World!");
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }

    @Bean
    public Job job() {
        return new JobBuilder("basicJob", jobRepository)
                .start(step())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldJob.class, args);
    }

}
