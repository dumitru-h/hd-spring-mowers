package hd.soft.mowItNow;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {TestConfig.class})
@EnableAutoConfiguration    // for Datasource bean
@SpringBatchTest
public class TestReader {

	@Autowired
	@Qualifier("testReader")
	ItemStreamReader<Tondeuse> reader;


	@Test
	public void testReader() throws Exception {

		reader.open(new ExecutionContext());

		assertEquals(
				new Tondeuse(Position.pos(1, 2, 'N'), 5, 5, "GAGAGAGAA"),
				reader.read()
		);

		assertEquals(
				new Tondeuse(Position.pos(3, 3, 'E'), 5, 5, "AADAADADDA"),
				reader.read()
		);

		reader.close();
	}

}
