package accelerator.insert.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Row {

    List<RowParameter> rowParameters;

    private Row() {
    }

    public Row(List<RowParameter> rowParameters) {
        this.rowParameters = rowParameters;
    }

    public List<String> getColumnNames() {
        return rowParameters
                .stream()
                .map(RowParameter::getColumnName)
                .collect(Collectors.toList());
    }

    public List<RowParameter> getRowParameters() {
        return rowParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return Objects.equals(rowParameters, row.rowParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowParameters);
    }

    @Override
    public String toString() {
        return "Row{" +
                "rowParameters=" + rowParameters +
                '}';
    }
}

