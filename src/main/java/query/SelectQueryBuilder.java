package query;

public class SelectQueryBuilder implements QueryBuilder {


    private final String[] fields;
    private String[] entities;
    private String whereClause;
    private String[] groupBy;
    private String havingClause;

    public SelectQueryBuilder(String... fields) {
        this.fields = fields;
        this.whereClause = "";
        this.groupBy = null;
        this.havingClause = "";
    }

    @Override
    public final String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(String.join(", ", fields));
        sb.append(" FROM ");
        sb.append(String.join(", ", entities));

        if (!whereClause.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(whereClause);
        }

        if (groupBy != null) {
            sb.append(" GROUP BY ");
            sb.append(String.join(", ", groupBy));
        }
        if (!havingClause.isEmpty()) {
            sb.append(" HAVING ");
            sb.append(havingClause);
        }
        sb.append(";");

        return sb.toString();
    }

    public SelectQueryBuilder from(String... entityName) {
        this.entities = entityName;
        return this;
    }


    public SelectQueryBuilder where(String clause) {
        this.whereClause = clause;
        return this;
    }

    public SelectQueryBuilder having(String clause) {
        this.havingClause = clause;
        return this;
    }

    public SelectQueryBuilder groupBy(String... fields) {
        this.groupBy = fields;
        return this;
    }

}
