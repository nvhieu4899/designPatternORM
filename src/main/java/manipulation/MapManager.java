package manipulation;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class MapManager {
    private static HashMap<String, BidiMap<String, String>> maps = new HashMap<>();

    // reading map info from file
    static {
        try {
            // open file to read
            FileReader reader = new FileReader("map.json");
            // parse to JSONArray object
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            // parse to JSONObject to save info into map
            for (int i = 0; i < jsonArray.size(); ++i) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String tableName = "";
                BidiMap<String, String> map = new DualHashBidiMap<>();

                // get keys
                for (Object key : jsonObject.keySet()) {
                    String keyStr = (String) key;

                    // check table's name
                    if (keyStr.equals("0"))
                        tableName = (String) jsonObject.get(key);
                    else {
                        map.put(keyStr, (String) jsonObject.get(key));
                    }
                }

                // add map
                maps.put(tableName, map);
            }

            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // get map by table's name
    public static BidiMap<String, String> getMap(String tableName) {
        return maps.get(tableName);
    }
}
