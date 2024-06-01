package education.ch04.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class HelloWorldTasklet implements Tasklet {

	private static final String HELLO_WORLD = "Hello, %s";

	private String name;

	public HelloWorldTasklet(String name) {
		this.name = name;
	}

	@Override
	public RepeatStatus execute(StepContribution step, ChunkContext context) {

/*
		String name = (String) context.getStepContext()
				.getJobParameters()
				.get("name");
*/

		ExecutionContext jobExecutionContext =
				context.getStepContext()
						.getStepExecution()
						.getJobExecution()
						.getExecutionContext();

		jobExecutionContext.put("user.name", this.name);

		System.out.println(String.format(HELLO_WORLD, this.name));

		return RepeatStatus.FINISHED;
	}
}
