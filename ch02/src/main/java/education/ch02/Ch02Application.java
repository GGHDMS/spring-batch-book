package education.ch02;

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

//@EnableBatchProcessing 필요가 없는듯 한데
@SpringBootApplication
public class Ch02Application {

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
        return new JobBuilder("job", jobRepository)
                .start(step())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Ch02Application.class, args);
    }

}
