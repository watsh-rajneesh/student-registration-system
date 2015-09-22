/*
 * Copyright (c) 2015 San Jose State University.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 */

package edu.sjsu.cohort6.esp.service;

import com.google.inject.Inject;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.dao.DBFactory;
import edu.sjsu.cohort6.esp.service.health.DBHealthCheck;
import edu.sjsu.cohort6.esp.service.rest.StudentResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
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
        /*
         * Register the static html contents to be served from /assets directory and accessible from browser from
         * http://<host>:<port>/esp
         */
        bootstrap.addBundle(new AssetsBundle("/assets", "/esp", "app.html"));
    }
    @Override
    public void run(StudentRegistrationServiceConfiguration studentRegistrationServiceConfiguration, Environment environment) throws Exception {
        client = studentRegistrationServiceConfiguration.getDbConfig().build(environment);
        environment.healthChecks().register("database", new DBHealthCheck(client));
        final StudentResource studentResource = new StudentResource(client);
        environment.jersey().setUrlPattern("/api/*");
        environment.jersey().register(studentResource);
    }
}
