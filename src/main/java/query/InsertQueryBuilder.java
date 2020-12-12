package query;

import javax.lang.model.type.PrimitiveType;
import java.util.ArrayList;

public class InsertQueryBuilder implements QueryBuilder {

    private final String entity;
    private String[] columns;
    private Object[] values;

    public InsertQueryBuilder(String entity) {
        this.entity = entity;
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(entity);
        if (columns.length > 0) {
            sb.append("(").append(String.join(", ", columns)).append(") ");
        }
        sb.append(" VALUES ").append("(").append(String.join(",", toStringParams())).append(")");
        sb.append(";");
        return sb.toString();
    }

    public InsertQueryBuilder columns(String... columns) {
        this.columns = columns;
        return this;
    }

    public InsertQueryBuilder values(Object... values) {
        this.values = values;
        return this;
    }

    private String[] toStringParams() {
        ArrayList<String> params = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            String res = Helper.toSqlStringType(value);
            params.add(res);
        }
        return params.toArray(params.toArray(new String[0]));
    }
}
