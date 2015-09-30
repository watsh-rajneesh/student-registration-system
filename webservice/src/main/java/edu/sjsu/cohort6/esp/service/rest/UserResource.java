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

package edu.sjsu.cohort6.esp.service.rest;

import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.service.rest.exception.InternalErrorException;
import io.dropwizard.auth.Auth;
import io.dropwizard.servlets.assets.ResourceNotFoundException;

import javax.validation.Valid;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * @author rwatsh on 9/24/15.
 */
public class UserResource extends BaseResource<User> {

    public UserResource(DBClient client) {
        super(client);
    }

    @Override
    public User create(@Auth User user, @Valid User model, @Context UriInfo info) {
        return null;
    }

    @Override
    public List list(@Auth User user) throws InternalErrorException {
        return null;
    }

    @Override
    public User retrieve(@Auth User user, @PathParam("id") String id) throws ResourceNotFoundException, InternalErrorException {
        return null;
    }

    @Override
    public User update(@Auth User user, @PathParam("id") String id, @Valid User entity) throws ResourceNotFoundException, InternalErrorException {
        return null;
    }

    @Override
    public int delete(@Auth User user, @PathParam("id") String id) throws ResourceNotFoundException, InternalErrorException {
        return null;
    }
}
