package accelerator.insert.domain;

import java.util.Objects;

public class RowParameter {

    private String columnName;
    private Class classOfParameter;
    private Object parameter;

    private RowParameter() {
    }

    public RowParameter(String columnName, Class classOfParameter, Object parameter) {
        this.columnName = columnName;
        this.classOfParameter = classOfParameter;
        this.parameter = parameter;
    }

    public Class getClassOfParameter() {
        return classOfParameter;
    }

    public Object getParameter() {
        return parameter;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RowParameter that = (RowParameter) o;
        return Objects.equals(classOfParameter, that.classOfParameter) &&
                Objects.equals(parameter, that.parameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classOfParameter, parameter);
    }

    @Override
    public String toString() {
        return "RowParameter{" +
                "classOfParameter=" + classOfParameter +
                ", parameter=" + parameter +
                '}';
    }

}
