package education.ch04.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
                .tasklet(helloWorldTasklet(null), transactionManager)
                .build();
    }

    @Bean
    public Job job() {
        return new JobBuilder("basicJob", jobRepository)
                .start(step())
                .build();
    }

/*
    @Bean
    public Tasklet helloWorldTasklet() {
        return (contribution, chunkContext) -> {
            String name = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("name");

            System.out.println(String.format("Hello, %s!", name));
            return RepeatStatus.FINISHED;
        };
    }
*/

    @Bean
    @StepScope
    public Tasklet helloWorldTasklet(@Value("#{jobParameters['name']}") String name) {
        return (contribution, chunkContext) -> {
            System.out.println(String.format("Hello, %s!", name));
            return RepeatStatus.FINISHED;
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldJob.class, args);
    }

}
