package edu.sjsu.cohort6.esp.dao.mongodb;

import com.google.inject.Inject;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * Created by rwatsh on 9/13/15.
 */
public class StudentDAO extends BasicDAO<Student, String> {
    @Inject
    protected StudentDAO(MongoClient mongoClient, Morphia morphia, String dbName) {
        super(mongoClient, morphia, dbName);
    }
}
