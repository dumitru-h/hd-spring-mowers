package hd.soft.mowItNow;

import hd.soft.mowItNow.batch.BatchConfiguration;
import hd.soft.mowItNow.batch.TondeuseReader;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {BatchConfiguration.class})
@EnableAutoConfiguration	// for Datasource bean
@SpringBatchTest
public class TestReader {

	@Autowired
	@Qualifier("itemReader")
	ItemReader<Tondeuse> reader;

	private final Resource testResource = new FileSystemResource("src/test/resources/input.txt");

	@Test
	public void testReader() throws Exception {
		TondeuseReader itemReader = (TondeuseReader)reader;
		itemReader.setResource(testResource);
		itemReader.open(new ExecutionContext());

		assertEquals(
				new Tondeuse(Position.pos(1, 2, 'N'), 5, 5, "GAGAGAGAA"),
				reader.read()
		);

		assertEquals(
				new Tondeuse(Position.pos(3, 3, 'E'), 5, 5, "AADAADADDA"),
				reader.read()
		);

		itemReader.close();
	}

}
