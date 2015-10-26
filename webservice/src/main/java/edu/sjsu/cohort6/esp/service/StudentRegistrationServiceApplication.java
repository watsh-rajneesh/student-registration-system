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

import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.service.auth.SimpleAuthenticator;
import edu.sjsu.cohort6.esp.service.cli.CreateUserCommand;
import edu.sjsu.cohort6.esp.service.cli.ListUserCommand;
import edu.sjsu.cohort6.esp.service.health.DBHealthCheck;
import edu.sjsu.cohort6.esp.service.rest.*;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by rwatsh on 9/14/15.
 */
public class StudentRegistrationServiceApplication extends Application<StudentRegistrationServiceConfiguration> {

    private DBClient dbClient;

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
        bootstrap.addBundle(new AssetsBundle("/assets", "/esp", "index.html"));

        bootstrap.addCommand(new CreateUserCommand());
        bootstrap.addCommand(new ListUserCommand());
    }

    @Override
    public void run(StudentRegistrationServiceConfiguration studentRegistrationServiceConfiguration, Environment environment) throws Exception {
        dbClient = studentRegistrationServiceConfiguration.getDbConfig().build(environment);

        /*
         * Setup basic authentication against DB table.
         */
        Authenticator<BasicCredentials, User> simpleAuthenticator = new SimpleAuthenticator(dbClient);
        environment.jersey().register(AuthFactory.binder(new BasicAuthFactory<User>(simpleAuthenticator,
                "studentreg", // realm name
                User.class))); // backing DB object

        environment.healthChecks().register("database", new DBHealthCheck(dbClient));

        /*
         * TODO: check - Setup CORS filter (may not be needed?)
         */
        environment.jersey().register(new CORSFilter());

        /*
         * Register resources with jersey.
         */
        final StudentResource studentResource = new StudentResource(dbClient);
        final CourseResource courseResource = new CourseResource(dbClient);
        final UserResource userResource = new UserResource(dbClient);

        /*
         * Setup jersey environment.
         */
        environment.jersey().setUrlPattern(EndpointUtils.ENDPOINT_ROOT + "/*");
        environment.jersey().register(studentResource);
        environment.jersey().register(courseResource);
        /*
         * Not registering user resource on purpose as student resource should be used to add a student user.
         * Admin user can only be added through the command line not exposed to users.
         */
        //environment.jersey().register(userResource);
    }
}
