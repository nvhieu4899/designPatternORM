package query;

public class Helper {
    static String toSqlStringType(Object object) {
        String res = object.toString();
        if (object instanceof String) {
            res = "'" + res + "'";
        }
        return res;
    }
}
