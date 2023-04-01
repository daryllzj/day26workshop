package paf;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import lombok.extern.slf4j.Slf4j;
import paf.day26_workshop_r1.model.Limitoffset;

@Slf4j
public class Utils {

    public static Limitoffset convertToLimitOffset(String payload) {
        
        JsonReader reader = Json.createReader(new StringReader(payload));
		JsonObject json = reader.readObject();

        Integer limit;
        Integer offset;
        try {
             limit = json.getInt("limit");
        } catch (Exception e) {
            limit = 25;
        }

        try {
            offset = json.getInt("offset");
       } catch (Exception e) {
           offset = 0;
       }

       Limitoffset limitoffset = new Limitoffset();
       limitoffset.setLimit(limit);
       limitoffset.setOffset(offset);

       log.info(limitoffset.toString());

       return limitoffset;
    }
    
}
