package hd.soft.mowItNow.batch;

import hd.soft.mowItNow.Position;
import hd.soft.mowItNow.Tondeuse;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static java.lang.System.out;

@Configuration
public class BatchConfiguration extends DefaultBatchConfiguration {

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
		return embeddedDatabaseBuilder
				.addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
				.addScript("classpath:org/springframework/batch/core/schema-h2.sql")
				.setType(EmbeddedDatabaseType.H2)
				.build();
	}

	@Bean
	public ItemReader<Tondeuse> itemReader() {
		// with injected param (@Value("#{jobParameters[inputFile]}") Resource resource
		return new TondeuseReader();
	}


	@Bean
	public ItemWriter<Position> itemWriter() {
		// with injected param (@Value("#{jobParameters[inputFile]}") Resource resource
		return new ItemWriter<Position>() {
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
						 ItemWriter<Position> itemWriter) {
		return new StepBuilder("step1", jobRepository)
				.<Tondeuse, Position> chunk(1, transactionManager)
				.reader(reader)
				.processor(t -> t.execute())
				.writer((Chunk<? extends Position> chunk) -> {
					chunk.getItems().forEach(position -> {
						System.out.print(position.outStr());
						System.out.print(" ");
					});
				}).build();
	}

	@Bean
	protected Job job(JobRepository jobRepository, Step step) {
		return new JobBuilder("mowerJob", jobRepository)
				.start(step)
				.build();
	}
}