package accelerator.insert.builder.impl;

import accelerator.insert.domain.Row;
import accelerator.insert.domain.RowParameter;
import accelerator.insert.builder.Builder;

import java.util.ArrayList;
import java.util.List;

public class RowParametersBuilder implements Builder {

    private Integer countOfColumnsToBeInserted;
    private List<Row> rows = new ArrayList<>();
    private List<RowParameter> rowParameters = new ArrayList<>();

    private RowParametersBuilder() {
    }

    public RowParametersBuilder(final Integer countOfColumnsToBeInserted) {
        this.countOfColumnsToBeInserted = countOfColumnsToBeInserted;
    }

    @Override
    public RowParametersBuilder createRowParameter(final String columnName,
                                                   final Class classOfParameter,
                                                   final Object parameter) {
        final RowParameter rowParameter = new RowParameter(columnName, classOfParameter, parameter);
        rowParameters.add(rowParameter);

        if (rowParameters.size() == countOfColumnsToBeInserted) {
            createRow(rowParameters);
            rowParameters = new ArrayList<>();
        }
        return this;
    }

    private void createRow(final List<RowParameter> rowParameters) {
        rows.add(new Row(rowParameters));
    }

    @Override
    public List<Row> build() {
        return rows;
    }

}
