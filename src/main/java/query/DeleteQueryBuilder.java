package query;

public class DeleteQueryBuilder implements QueryBuilder {


    private final String entity;
    private String whereClause;


    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(entity);
        sb.append(" WHERE ");
        sb.append(whereClause);
        sb.append(";");
        return sb.toString();
    }

    public DeleteQueryBuilder(String entity) {
        this.entity = entity;
    }

    public DeleteQueryBuilder where(String clause) {
        this.whereClause = clause;
        return this;
    }
}
