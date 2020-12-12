package query;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateQueryBuilder implements QueryBuilder {

    private String entity;
    private HashMap<String, Object> values = new HashMap<>();

    private String whereClause;

    public UpdateQueryBuilder(String entity) {
        this.entity = entity;
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(entity).append(" SET ").append(getSetClause()).append(" WHERE ").append(whereClause).append(";");
        return sb.toString();
    }


    public UpdateQueryBuilder where(String clause) {
        this.whereClause = clause;
        return this;
    }

    public UpdateQueryBuilder set(String key, Object value) {
        values.put(key, value);
        return this;
    }

    private String getSetClause() {
        ArrayList<String> params = new ArrayList<>();
        for (String key : values.keySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(key).append("=").append(Helper.toSqlStringType(values.getOrDefault(key, 0)));
            params.add(sb.toString());
        }
        return String.join(", ", params);
    }
}
