package edu.sjsu.cohort6.esp.dao;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by rwatsh on 9/7/15.
 */
public class DaoUtil {

    public static DataModel convertJsonToDataModel(String json) {
        DataModel d = null;
        DBObject dbObject = (DBObject) JSON.parse(json);

        if (json.contains("courseName")) {
            d = new Course();
            ((Course)d).setCourseName((String) dbObject.get("courseName"));
            List<String> studentRefsList = new ArrayList<>();
            BasicDBList students = (BasicDBList) dbObject.get("students");
            for (int i = 0; students != null && i <  students.size(); i++) {
                studentRefsList.add(students.get(i).toString());
            }
            ((Course)d).setStudentRefs(studentRefsList);
        } else {
            d = new Student();
            ((Student)d).setFirstName((String) dbObject.get("firstName"));
            ((Student)d).setLastName((String) dbObject.get("lastName"));
            ((Student)d).setEmailId((String) dbObject.get("emailId"));
            List<String> courseRefsList = new ArrayList<>();
            BasicDBList courses = (BasicDBList) dbObject.get("courses");
            for (int i = 0; courses != null && i <  courses.size(); i++) {
                courseRefsList.add(courses.get(i).toString());
            }
            ((Student)d).setCourseRefs(courseRefsList);
        }
        return d;
    }
}
