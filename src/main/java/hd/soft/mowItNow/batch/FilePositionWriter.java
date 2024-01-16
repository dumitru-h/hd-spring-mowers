package hd.soft.mowItNow.batch;

import hd.soft.mowItNow.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.stream.Collectors.joining;

@Slf4j
public class FilePositionWriter implements ItemStreamWriter<Position> {
	Writer fileWriter;

	private final String fileName;

	public FilePositionWriter(String fileName) {
		this.fileName = fileName;
	}

	private static String fileTimeStamp() {
		return
				ISO_DATE_TIME.format(LocalDateTime.now())
						.replace(":", "")
						.replace("-", "")
				;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		File file = new File(fileName);
		while (file.exists()) {
			file = new File(file.getParent(), file.getName() + "_" + fileTimeStamp());
		}

		ItemStreamWriter.super.open(executionContext);
		try {
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws ItemStreamException {
		ItemStreamWriter.super.close();

		try {
			fileWriter.close();
		} catch (IOException e) {
			log.error("IOException on file " + fileName + "close. Possible resource leak.");
			throw new RuntimeException(e);
		}
	}

	@Override
	public void write(Chunk<? extends Position> chunk) throws Exception {
		String outStr = chunk.getItems().stream()
				.map(p -> p.outStr())
				.collect(joining(String.valueOf(' ')));
		fileWriter.write(outStr + ' ');
	}
}
