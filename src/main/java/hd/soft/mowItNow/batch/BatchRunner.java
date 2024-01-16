package hd.soft.mowItNow.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static hd.soft.mowItNow.batch.BatchConfiguration.BATCH_OUTPUT_FILE;
import static hd.soft.mowItNow.batch.BatchConfiguration.BATCH_PARAM_FILE;
import static java.lang.System.exit;

@Component
@Slf4j
public class BatchRunner implements CommandLineRunner {
	public static final int
			EXIT_NOT_COMPLETED = 1,
			EXIT_NO_INPUT = 2;

	private final JobLauncher jobLauncher;
	private final Job job;

	@Autowired
	private ApplicationArguments appArgs;

	public BatchRunner(JobLauncher jobLauncher, Job defaultJob) {
		this.jobLauncher = jobLauncher;
		this.job = defaultJob;
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("applicationArguments: {}", appArgs.getOptionNames());

		if (appArgs.containsOption(BATCH_PARAM_FILE)) {
			String file = appArgs.getOptionValues(BATCH_PARAM_FILE).get(0);
			JobParametersBuilder paramsBuilder = new JobParametersBuilder()
					.addString(BATCH_PARAM_FILE, file);

			if (appArgs.containsOption(BATCH_OUTPUT_FILE)) {
				paramsBuilder.addString(BATCH_OUTPUT_FILE, appArgs.getOptionValues(BATCH_OUTPUT_FILE).get(0));
			} else {
				log.info("--fileOutput parameter  xwas not specified. Will output to stdout");
			}

			JobExecution execution = jobLauncher.run(job, paramsBuilder.toJobParameters());
			if (!execution.getExitStatus().equals(ExitStatus.COMPLETED)) {
				log.error("job finished with exit status: " + execution.getExitStatus());
				exit(EXIT_NOT_COMPLETED);
			}
		} else {
			log.error("Missing command line argument: {}", BATCH_PARAM_FILE);
			exit(EXIT_NO_INPUT);
		}
	}
}
