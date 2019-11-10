package accelerator.insert.builder;

import accelerator.insert.builder.impl.RowParametersBuilder;
import accelerator.insert.domain.Row;

import java.util.List;

public interface Builder {

    List<Row> build();

    RowParametersBuilder createRowParameter(String columnName, Class classOfParameter, Object parameter);

}
