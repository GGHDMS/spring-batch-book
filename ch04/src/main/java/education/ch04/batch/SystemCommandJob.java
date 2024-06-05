package education.ch04.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.SimpleSystemProcessExitCodeMapper;
import org.springframework.batch.core.step.tasklet.SystemCommandTasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

//@SpringBootApplication
public class SystemCommandJob {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Bean
	public Job job() {
		return new JobBuilder("systemCommandJob", jobRepository)
				.start(systemCommandStep())
				.build();
	}

	@Bean
	public Step systemCommandStep() {
		return new StepBuilder("systemCommandStep", jobRepository)
				.tasklet(systemCommandTasklet(), transactionManager)
				.build();
	}

	@Bean
	public SystemCommandTasklet systemCommandTasklet() {
		SystemCommandTasklet systemCommandTasklet = new SystemCommandTasklet();

		systemCommandTasklet.setCommand("rm -rf /tmp.txt");
		systemCommandTasklet.setTimeout(5000);
		systemCommandTasklet.setInterruptOnCancel(true);

		return systemCommandTasklet;
	}

/*
	@Bean
	public SystemCommandTasklet systemCommandTasklet() {
		SystemCommandTasklet tasklet = new SystemCommandTasklet();

		tasklet.setCommand("touch tmp.txt");
		tasklet.setTimeout(5000);
		tasklet.setInterruptOnCancel(true);

		tasklet.setWorkingDirectory("/Users/edutilos/Desktop/education/spring-batch-education/");

		tasklet.setSystemProcessExitCodeMapper(touchCodeMapper());
		tasklet.setTerminationCheckInterval(5000);
		tasklet.setTaskExecutor(new SimpleAsyncTaskExecutor());
		tasklet.setEnvironmentParams(new String[] {
				"JAVA_HOME=/java",
				"BATCH_HOME=/Users/batch"});

		return tasklet;
	}
*/

	@Bean
	public SimpleSystemProcessExitCodeMapper touchCodeMapper() {
		return new SimpleSystemProcessExitCodeMapper();
	}


	public static void main(String[] args) {
		SpringApplication.run(SystemCommandJob.class, args);
	}
}
