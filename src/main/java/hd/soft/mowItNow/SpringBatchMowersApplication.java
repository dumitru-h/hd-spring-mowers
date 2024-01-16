package hd.soft.mowItNow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class SpringBatchMowersApplication {

	public static void main(String[] args) {
		System.out.println("main args: " + Arrays.stream(args).toList());

		System.exit(SpringApplication.exit(SpringApplication.run(SpringBatchMowersApplication.class, args)));
	}
}
