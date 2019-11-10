package accelerator.insert;

import accelerator.insert.domain.RowParameter;
import accelerator.insert.domain.Row;
import accelerator.utils.ListPartitionDivider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public class InsertQueryBuilder {

    private static final String COMMA = ", ";
    private static final String SEMICOLON = "; ";
    private static final String APOSTROPHE = "'";
    private static final String DOUBLE_APOSTROPHE = "''";

    private static final Integer FIRST_ELEMENT = 0;
    private static final Object NULL_VALUE = null;

    private static final Integer SQL_INSERT_PARTITION_SIZE = 1000;

    public List<String> build(final String fullDbSchemaTableName, final List<Row> rows) {
        final String insertIntoPartOfQuery = buildInsertIntoPartOfQuery(fullDbSchemaTableName, rows.get(FIRST_ELEMENT));

        final List<StringBuilder> insertStatements = new ArrayList<>();
        ListPartitionDivider
                .divideListIntoPartitionsOfSize(rows, SQL_INSERT_PARTITION_SIZE)
                .forEach(subList -> insertStatements
                        .add(buildValuesPartOfQuery(insertIntoPartOfQuery, subList))
                );
        return convertToListOfStringObjects(insertStatements);
    }

    private String buildInsertIntoPartOfQuery(final String fullDbSchemaTableName,
                                                     final Row row) {
        return String.format("INSERT INTO %s ( %s )",
                fullDbSchemaTableName,
                join(COMMA, row.getColumnNames())
        );
    }

    private StringBuilder buildValuesPartOfQuery(final String insertIntoPartOfQuery,
                                                        final List<Row> partitionList) {
        final StringBuilder prefix = buildPrefixPartOfValuesQuery(insertIntoPartOfQuery);

        final StringBuilder body = new StringBuilder();
        partitionList.forEach(row -> buildBodyPartOfValuesQuery(body, row));

        replaceCommaWithSemicolonInLastQuery(body);

        return mergeInsertIntoAndValuePartsOfQuery(prefix, body);
    }

    private StringBuilder buildPrefixPartOfValuesQuery(final String query) {
        return new StringBuilder()
                .append(query)
                .append(" VALUES ");
    }

    private void buildBodyPartOfValuesQuery(final StringBuilder body, final Row row) {
        body.append("(");
        row.getRowParameters().forEach(parameter -> {
            body.append(isNull(parameter.getParameter()) ? NULL_VALUE : parseParameter(parameter));

            if (isNotLastParameter(row, parameter)) {
                body.append(", ");
            }
        });
        body.append("),");
    }

    private StringBuilder parseParameter(final RowParameter parameter) {
        final String parameterClassName = parameter.getClassOfParameter().getCanonicalName();
        if (String.class.getCanonicalName().equals(parameterClassName)) {
            return new StringBuilder().append(APOSTROPHE
                    .concat(String.valueOf(parameter.getParameter()).replaceAll(APOSTROPHE, DOUBLE_APOSTROPHE))
                    .concat(APOSTROPHE));
        } else if (Boolean.class.getCanonicalName().equals(parameterClassName)) {
            return new StringBuilder(String.valueOf((Boolean) parameter.getParameter() ? 1 : 0));
        } else if (isParameterOfNumberType(parameterClassName)) {
            return new StringBuilder(String.valueOf(parameter.getParameter()));
        } else {
            throw new UnsupportedOperationException(String.format("Class %s of your parameter " +
                    "is not supported yet.", parameter.getClassOfParameter().getCanonicalName()));
        }
    }

    private boolean isParameterOfNumberType(final String parameterClassName) {
        return Integer.class.getCanonicalName().equals(parameterClassName)
                || Float.class.getCanonicalName().equals(parameterClassName)
                || Double.class.getCanonicalName().equals(parameterClassName)
                || BigDecimal.class.getCanonicalName().equals(parameterClassName);
    }

    private boolean isNotLastParameter(final Row row, final RowParameter parameter) {
        return row.getRowParameters().indexOf(parameter) != (row.getRowParameters().size() -1);
    }

    private void replaceCommaWithSemicolonInLastQuery(final StringBuilder body) {
        body.replace(body.length() - 1, body.length(), SEMICOLON);
    }

    private StringBuilder mergeInsertIntoAndValuePartsOfQuery(final StringBuilder prefix,
                                                                     final StringBuilder body) {
        prefix.append(body);
        return prefix;
    }

    private List<String> convertToListOfStringObjects(final List<StringBuilder> insertStatements) {
        return insertStatements.stream().map(StringBuilder::toString).collect(toList());
    }

}
