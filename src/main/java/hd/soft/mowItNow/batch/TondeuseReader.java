package hd.soft.mowItNow.batch;

import hd.soft.mowItNow.Position;
import hd.soft.mowItNow.Tondeuse;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.Resource;

import static hd.soft.mowItNow.Position.pos;

public class TondeuseReader implements ItemReader<Tondeuse>, ItemStream {

	private FlatFileItemReader<FieldSet> delegate =new FlatFileItemReaderBuilder<FieldSet>().name("delegateItemReader")
			.lineTokenizer(new DelimitedLineTokenizer(String.valueOf(' ')))
			.fieldSetMapper(new PassThroughFieldSetMapper())
			.build();;

	private int maxX, maxY;

	public void setResource(Resource resource) {
		delegate.setResource(resource);
	}

	@Override
	public Tondeuse read() throws Exception {
		FieldSet line1 = this.delegate.read();
		FieldSet line2= this.delegate.read();

		if (line1 != null && line2!=null) {
			final Position initialPosition = pos(
					line1.readInt(0),
					line1.readInt(1),
					line1.readChar(2)
					);
			final String program = line2.readString(0);

			return new Tondeuse(initialPosition, maxX, maxY, program);
		}

		return null;
	}

	@Override
	public void close() throws ItemStreamException {
		this.delegate.close();
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		this.delegate.open(executionContext);

		try {
			FieldSet headerLine = this.delegate.read();
			maxX = headerLine.readInt(0);
			maxY = headerLine.readInt(1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		this.delegate.update(executionContext);
	}
}
