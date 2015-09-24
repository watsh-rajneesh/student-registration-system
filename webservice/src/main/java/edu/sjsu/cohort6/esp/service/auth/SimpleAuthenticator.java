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

package edu.sjsu.cohort6.esp.service.auth;

import com.google.common.base.Optional;
import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.dao.mongodb.UserDAO;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.logging.Logger;

/**
 * Setup basic authenticator which has backing User DB table to store the user credentials.
 *
 * @author rwatsh
 */
public class SimpleAuthenticator implements Authenticator<BasicCredentials, User> {
    private DBClient dbClient;
    private static final Logger log = Logger.getLogger(SimpleAuthenticator.class.getName());

    public SimpleAuthenticator(DBClient dbClient) {
        this.dbClient = dbClient;
    }


    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        return getUserByCredentials(credentials.getUsername(), credentials.getPassword());
    }

    private Optional<User> getUserByCredentials(String username, String password) {
        UserDAO userDAO = (UserDAO) dbClient.getDAO(UserDAO.class);
        return userDAO.getUserByCredentials(username, password);
    }
}
