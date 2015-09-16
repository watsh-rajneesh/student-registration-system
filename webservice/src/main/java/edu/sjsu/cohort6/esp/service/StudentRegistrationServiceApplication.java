package edu.sjsu.cohort6.esp.service;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.dao.DBFactory;
import edu.sjsu.cohort6.esp.dao.DatabaseModule;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by rwatsh on 9/14/15.
 */
public class StudentRegistrationServiceApplication extends Application<StudentRegistrationServiceConfiguration> {

    @Inject
    DBFactory dbFactory;
    private DBClient client;

    public static void main(final String[] args) throws Exception {
        new StudentRegistrationServiceApplication().run(args);

    }

    @Override
    public String getName() {
        return "StudentRegistrationService";
    }

    @Override
    public void initialize(final Bootstrap<StudentRegistrationServiceConfiguration> bootstrap) {
        // TODO: application initialization
        Module module = new DatabaseModule();
        Guice.createInjector(module).injectMembers(this);
        client = dbFactory.create("localhost", 27017, "esp");
    }
    @Override
    public void run(StudentRegistrationServiceConfiguration studentRegistrationServiceConfiguration, Environment environment) throws Exception {

    }
}
