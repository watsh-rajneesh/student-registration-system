package edu.sjsu.cohort6.esp.dao;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * Created by rwatsh on 9/7/15.
 */
public class DaoUtil {

    public static DataModel convertJsonToDataModel(String json) {
        DataModel d = null;
        if (json.contains("courseName")) {
            d = new Course();
        } else {
            d = new Student();
        }
        DBObject dbObject = (DBObject) JSON.parse(json);
        dbObject.get()
    }
}
