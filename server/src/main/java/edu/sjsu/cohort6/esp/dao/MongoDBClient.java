package edu.sjsu.cohort6.esp.dao;

import com.mongodb.*;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rwatsh on 9/7/15.
 */
public class MongoDBClient {
    private MongoClient mongoClient;
    private DB database;
    private DBCollection collection;

    public MongoDBClient(String server, int port, String db) throws UnknownHostException {
        // To directly connect to a single MongoDB server
        MongoClient mongoClient = new MongoClient(new MongoClientURI(MessageFormat.format("mongodb://{0}:{1}", server, Integer.toString(port))));
        database = mongoClient.getDB(db);

    }

    public void insert(DataModel model) {
        collection = database.getCollection(model.getName());
        DBObject dbObject = model.toDBObject();
        collection.insert(dbObject);
    }

    public void getAll(String collectionName) {
        collection = database.getCollection(collectionName);
        DBCursor cursorDocBuilder = collection.find();
        List<DataModel> dataModelsList = new ArrayList<DataModel>();

        try {
            while (cursorDocBuilder.hasNext()) {
                DataModel d = DaoUtil.convertJsonToDataModel(cursorDocBuilder.next());
                dataModelsList.add(d);
            }
        } finally {
            cursorDocBuilder.close();
            collection.remove(new BasicDBObject());
        }

    }




    public static void main(String[] args) throws UnknownHostException {
        MongoDBClient mongoDBClient = null;
        try {
            mongoDBClient = new MongoDBClient("localhost", 27017, "test");
            mongoDBClient.getAll("course");
            mongoDBClient.getAll("student");

            Course course = new Course("testcourse");
            mongoDBClient.insert(course);
            mongoDBClient.getAll("course");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
