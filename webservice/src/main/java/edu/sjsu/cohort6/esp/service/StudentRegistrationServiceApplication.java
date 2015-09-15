package edu.sjsu.cohort6.esp.service;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by rwatsh on 9/14/15.
 */
public class StudentRegistrationServiceApplication extends Application<StudentRegistrationServiceConfiguration> {

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
    }
    @Override
    public void run(StudentRegistrationServiceConfiguration studentRegistrationServiceConfiguration, Environment environment) throws Exception {

    }
}
