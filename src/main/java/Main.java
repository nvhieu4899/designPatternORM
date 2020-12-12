import query.QueryBuilder;

public class Main {
    public static void main(String[] args) {
        String query1 = QueryBuilder.select("name", "age").from("SINHVIEN").where("mssv > 14").build();
        System.out.println(query1);
        String query2 = QueryBuilder.insertInto("SINHVIEN").columns("name", "age").values("Hieu", 18).build();
        System.out.println(query2);
        String query3 = QueryBuilder.update("SINHVIEN").set("name", "hieu").set("age", 21).where("name = 'Hieu'").build();
        System.out.println(query3);
        String query4 = QueryBuilder.deleteFrom("SINHVIEN").where("name = 'hieu'").build();
        System.out.println(query4);
    }
}
