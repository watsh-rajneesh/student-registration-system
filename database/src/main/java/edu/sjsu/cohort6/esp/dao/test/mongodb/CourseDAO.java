package edu.sjsu.cohort6.esp.dao.test.mongodb;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * Created by rwatsh on 9/13/15.
 */
public class CourseDAO extends BasicDAO<Course, String> {
    protected CourseDAO(MongoClient mongoClient, Morphia morphia, String dbName) {
        super(mongoClient, morphia, dbName);
    }
}
