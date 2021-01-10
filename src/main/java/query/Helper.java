package query;

import java.sql.Date;

public class Helper {
    static String toSqlStringType(Object object) {
        String res = object.toString();
        if (object instanceof String && !object.equals("?") || object instanceof Date) {
            res = "'" + res + "'";
        }
        return res;
    }
}
