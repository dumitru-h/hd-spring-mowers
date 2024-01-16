package hd.soft.mowItNow;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;

import static hd.soft.mowItNow.batch.BatchConfiguration.BATCH_OUTPUT_FILE;
import static hd.soft.mowItNow.batch.BatchConfiguration.BATCH_PARAM_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@ContextConfiguration(classes = {TestConfig.class})
@EnableAutoConfiguration
@SpringBatchTest
public class JobTest {

	@Autowired
	@Qualifier("testReader")
	ItemStreamReader<Tondeuse> reader;

	private final Resource testResource = new FileSystemResource("src/test/resources/input.txt");

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void testJob(@Autowired Job job) throws Exception {
		jobLauncherTestUtils.setJob(job);

		JobParameters jobParameters = new JobParametersBuilder()
				.addString(BATCH_PARAM_FILE, "src/test/resources/input.txt")
				.addString(BATCH_OUTPUT_FILE, "target/jobOutput.txt")
				.toJobParameters();

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		assertEquals(COMPLETED, jobExecution.getExitStatus());
	}
}
