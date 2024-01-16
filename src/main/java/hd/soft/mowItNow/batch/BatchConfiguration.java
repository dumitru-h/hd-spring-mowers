package hd.soft.mowItNow.batch;

import hd.soft.mowItNow.Position;
import hd.soft.mowItNow.Tondeuse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;

import static java.lang.System.out;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Configuration
@Slf4j
public class BatchConfiguration extends DefaultBatchConfiguration {
	public static final String BATCH_PARAM_FILE = "inputFile";
	public static final String BATCH_OUTPUT_FILE = "outputFile";

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
	@StepScope
	public ItemStreamReader<Tondeuse> itemReader(@Value("#{jobParameters['inputFile']}") String inputFile) {
		TondeuseReader tondeuseReader = new TondeuseReader();
		if (inputFile != null) {
			tondeuseReader.setResource(new FileSystemResource(inputFile));
		}
		return tondeuseReader;
	}

	private static String fileTimeStamp() {
		return
				ISO_DATE_TIME.format(LocalDateTime.now())
						.replace(":", "")
						.replace("-", "")
				;
	}

	private ItemStreamWriter<Position> fileItemWriter(String fileName) {
		return new FilePositionWriter(fileName);
	}


	@Bean
	@StepScope
	public ItemStreamWriter<Position> itemWriter(@Value("#{jobParameters['outputFile']}") String outputFile) {

		if (isNotBlank(outputFile)) {
			log.info("will write to {}", outputFile);
			return new FilePositionWriter(outputFile.trim());
		}

		log.info("will write to stdout", outputFile);
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
