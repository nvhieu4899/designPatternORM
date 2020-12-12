package query;

public interface QueryBuilder {

    String build();

    static SelectQueryBuilder select(String... fieldsName) {
        return new SelectQueryBuilder(fieldsName);
    }

    static UpdateQueryBuilder update(String entityName) {
        return new UpdateQueryBuilder(entityName);
    }

    static InsertQueryBuilder insertInto(String entityName) {
        return new InsertQueryBuilder(entityName);
    }

    static DeleteQueryBuilder deleteFrom(String entityName) {
        return new DeleteQueryBuilder(entityName);
    }
}
