package edu.sjsu.cohort6.esp.dao.test;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import edu.sjsu.cohort6.esp.dao.test.mongodb.MongoDBClient;

/**
 * Setup Guice bindings.
 *
 * @author rwatsh
 */
public class DatabaseModule extends AbstractModule {
    @Override
    public void configure() {
        install(new FactoryModuleBuilder()
                .implement(DBClient.class, MongoDBClient.class)
                .build(DBFactory.class));
    }
}
