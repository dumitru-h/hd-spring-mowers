package hd.soft.mowItNow;

import hd.soft.mowItNow.batch.TondeuseReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import static java.lang.System.out;

@TestConfiguration
public class TestConfig {

	private final Resource testResource = new FileSystemResource("src/test/resources/input.txt");

	@Bean
	@Scope("prototype")
	public ItemStreamReader<Tondeuse> testReader() {
		TondeuseReader itemReader = new TondeuseReader();
		itemReader.setResource(testResource);

		return itemReader;
	}

	@Bean
	@Scope("prototype")
	public ItemStreamWriter<Position> itemWriter() {
		return new ItemStreamWriter<Position>() {
			@Override
			public void write(Chunk<? extends Position> chunk) throws Exception {
				chunk.getItems().forEach(position -> {
					out.print(position.outStr());
					out.print(" ");
				});
			}
		};
	}

	@Bean
	protected Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
						 ItemReader<Tondeuse> reader,
						 ItemWriter<Position> writer) {
		return new StepBuilder("run 1 mower at a time", jobRepository)
				.<Tondeuse, Position>chunk(1, transactionManager)
				.reader(reader)
				.processor(t -> t.execute())
				.writer(writer)
				.build();
	}

	@Bean
	protected Job job(JobRepository jobRepository, Step step) {
		return new JobBuilder("mowerJob", jobRepository)
				.start(step)
				.build();
	}
}
