package education.ch04.jobs;

import education.ch04.batch.RandomDecider;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class ConditionalJob {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public Tasklet passTasklet() {
        return (contribution, chunkContext) -> {
//			return RepeatStatus.FINISHED;
            throw new RuntimeException("Causing a failure");
        };
    }

    @Bean
    public Tasklet successTasklet() {
        return (contribution, context) -> {
            System.out.println("Success!");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet failTasklet() {
        return (contribution, context) -> {
            System.out.println("Failure!");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Job job() {
        return new JobBuilder("conditionalJob", jobRepository)
                .start(firstStep())
                .on("FAILED").end()
                .from(firstStep()).on("*").to(successStep())
                .end()
                .build();
    }

    @Bean
    public Step firstStep() {
        return new StepBuilder("firstStep", jobRepository)
                .tasklet(passTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step successStep() {
        return new StepBuilder("successStep", jobRepository)
                .tasklet(successTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step failureStep() {
        return new StepBuilder("failureStep", jobRepository)
                .tasklet(failTasklet(), transactionManager)
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new RandomDecider();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConditionalJob.class, args);
    }
}
