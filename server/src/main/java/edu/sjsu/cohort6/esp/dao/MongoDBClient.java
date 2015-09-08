package edu.sjsu.cohort6.esp.dao;

import com.mongodb.*;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static edu.sjsu.cohort6.esp.dao.DaoUtil.*;

/**
 * Created by rwatsh on 9/7/15.
 */
public class MongoDBClient implements AutoCloseable {
    private MongoClient mongoClient;
    private DB database;
    private DBCollection collection;

    public MongoDBClient(String server, int port, String db) throws UnknownHostException {
        // To directly connect to a single MongoDB server
        MongoClient mongoClient = new MongoClient(new MongoClientURI(MessageFormat.format("mongodb://{0}:{1}", server, Integer.toString(port))));
        database = mongoClient.getDB(db);

    }

    public DBObject insert(DataModel model) {
        collection = database.getCollection(model.getName());
        DBObject dbObject = model.toDBObject();
        collection.insert(dbObject);
        return dbObject;
    }


    public DBObject update(DataModel model, String id) {
        collection = database.getCollection(model.getName());
        DBObject updatedDBObj = model.toDBObject();
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("_id", MessageFormat.format("{\"$oid\": \"{0}\"}", id));

        collection.update(searchQuery, updatedDBObj);
        return updatedDBObj;
    }


    public List<DataModel> getAll(String collectionName) {
        collection = database.getCollection(collectionName);
        DBCursor cursorDocBuilder = collection.find();
        List<DataModel> dataModelsList = new ArrayList<DataModel>();

        try {
            while (cursorDocBuilder.hasNext()) {
                String json = cursorDocBuilder.next().toString();
                DataModel d = convertJsonToDataModel(json);
                dataModelsList.add(d);
                System.out.println(json);
            }
        } finally {
            cursorDocBuilder.close();
            //collection.remove(new BasicDBObject());
        }
        return dataModelsList;
    }




    public static void main(String[] args) throws UnknownHostException {
        try (MongoDBClient mongoDBClient = new MongoDBClient("localhost", 27017, "test")){
            mongoDBClient.getAll("course");
            mongoDBClient.getAll("student");
            UUID uuid = UUID.randomUUID();

            // Add a new course
            Course course = new Course("testcourse"+ uuid.toString());
            DBObject newCourse = mongoDBClient.insert(course);
            mongoDBClient.getAll("course");


            // Add a new student
            Student student = new Student("Watsh"+ uuid.toString(), "Rajneesh", "watsh.rajneesh@sjsu.edu");
            DBObject newStudent = mongoDBClient.insert(student);
            mongoDBClient.getAll("student");


            // Enroll student to course
            List<String> newCourseRefs = new ArrayList<>();
            newCourseRefs.add(newCourse.get("_id").toString());
            student.enrollCourses(newCourseRefs);
            mongoDBClient.update(student, newStudent.get("_id").toString());

            mongoDBClient.getAll("student");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void close() throws Exception {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
