package hd.soft.mowItNow;

import hd.soft.mowItNow.batch.BatchConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {BatchConfiguration.class})
@EnableAutoConfiguration
@SpringBatchTest
public class JobStepTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void testJob(@Autowired Job job) throws Exception {
		jobLauncherTestUtils.setJob(job);

		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
	}
}
