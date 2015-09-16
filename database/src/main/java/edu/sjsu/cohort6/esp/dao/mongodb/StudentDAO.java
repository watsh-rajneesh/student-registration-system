package edu.sjsu.cohort6.esp.dao.mongodb;

import com.mongodb.MongoClient;
import edu.sjsu.cohort6.esp.common.Student;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * Created by rwatsh on 9/13/15.
 */
public class StudentDAO extends BasicDAO<Student, String> {
    protected StudentDAO(MongoClient mongoClient, Morphia morphia, String dbName) {
        super(mongoClient, morphia, dbName);
    }
}