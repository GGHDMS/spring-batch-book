package education.ch04.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class GoodByeTasklet implements Tasklet {

	private static final String BYE_WORLD = "Bye, %s";
	private String name;

	public GoodByeTasklet(String name) {
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

		System.out.println(String.format(BYE_WORLD, this.name));

		return RepeatStatus.FINISHED;
	}
}
