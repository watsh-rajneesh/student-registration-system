package edu.sjsu.cohort6.esp.dao;

import com.google.inject.AbstractModule;
import edu.sjsu.cohort6.esp.dao.mongodb.MongoDBClient;

/**
 * Setup Guice bindings.
 *
 * @author rwatsh
 */
public class StudentRegistrationModule extends AbstractModule {
    @Override
    protected void configure() {
        // Currently there is only one DB supported - MongoDB
        bind(DBClient.class).to(MongoDBClient.class);
    }
}
