package hd.soft.mowItNow;

import hd.soft.mowItNow.batch.TondeuseReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

	@Bean
	public ItemStreamReader<Tondeuse> testReader() {
		return new TondeuseReader();
	}
}
