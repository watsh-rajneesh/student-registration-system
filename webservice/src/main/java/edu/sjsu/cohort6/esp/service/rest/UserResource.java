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

import edu.sjsu.cohort6.esp.common.CommonUtils;
import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.service.rest.exception.AuthorizationException;
import edu.sjsu.cohort6.esp.service.rest.exception.BadRequestException;
import edu.sjsu.cohort6.esp.service.rest.exception.InternalErrorException;
import edu.sjsu.cohort6.esp.service.rest.exception.ResourceNotFoundException;
import io.dropwizard.auth.Auth;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rwatsh on 9/24/15.
 */
@Path(EndpointUtils.ENDPOINT_ROOT + "/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource extends BaseResource<User> {

    private static final Logger log = Logger.getLogger(UserResource.class.getName());

    public UserResource(DBClient client) {
        super(client);
    }

    @Override
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Auth User user1, @Valid String userJson, @Context UriInfo info) {
        try {
            if (isAdminUser(user1)) {
                User user = CommonUtils.convertJsonToObject(userJson, User.class);
                List<User> userList = new ArrayList<>();
                userList.add(user);
                userDAO.add(userList);
                URI uri = UriBuilder.fromResource(UserResource.class).build(user.getId());
                return Response.created(uri)
                        .entity(Entity.json(user))
                        .build();
            } else {
                throw new AuthorizationException("User " + user1.getUserName() + " is not allowed to perform this operation");
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error in adding user", e);
            throw new BadRequestException();
        }
    }

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List list(@Auth User user, @QueryParam("filter") String filter) throws InternalErrorException {
        if (isAdminUser(user)) {
            List<String> userIds = new ArrayList<>();
            List<User> userList = userDAO.fetchById(userIds);
            return userList;
        } else {
            throw new AuthorizationException("User " + user.getUserName() + " is not allowed to perform this operation");
        }
    }

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public User retrieve(@Auth User user, @PathParam("id") String id) throws ResourceNotFoundException, InternalErrorException {
        if (isAdminUser(user)) {
            List<String> userIdList = getListFromEntityId(id);
            List<User> userList = userDAO.fetchById(userIdList);
            if (userList != null && !userList.isEmpty()) {
                return userList.get(0);
            } else {
                throw new ResourceNotFoundException();
            }
        } else {
            throw new AuthorizationException("User " + user.getUserName() + " is not allowed to perform this operation");
        }
    }

    /**
     * Update any user.
     * This method is only accessible to admin user.
     * Partial update of user field is not supported yet.
     * Student user can use students endpoint to update his/her user info which supports partial updates.
     *
     * @param user1
     * @param id
     * @param userJson
     * @return
     * @throws ResourceNotFoundException
     * @throws InternalErrorException
     * @throws IOException
     */
    @Override
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public User update(@Auth User user1, @PathParam("id") String id, @Valid String userJson) throws ResourceNotFoundException, InternalErrorException, IOException {
        if (isAdminUser(user1)) {
            User user = CommonUtils.convertJsonToObject(userJson, User.class);
            user.setId(id);
            try {
                userDAO.update(getListFromEntity(user));
                List<User> userList = userDAO.fetchById(getListFromEntityId(id));
                if (userList != null && !userList.isEmpty()) {
                    return userList.get(0);
                }
                throw new ResourceNotFoundException();
            } catch (Exception e) {
                throw new InternalErrorException(e);
            }
        } else {
            throw new AuthorizationException("User " + user1.getUserName() + " is not allowed to perform this operation");
        }
    }

    @Override
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@Auth User user, @PathParam("id") String id) throws ResourceNotFoundException, InternalErrorException {
        try {
            if (isAdminUser(user)) {
                userDAO.remove(getListFromEntityId(id));
                return Response.ok().build();
            } else {
                throw new AuthorizationException("User " + user.getUserName() + " is not allowed to perform this operation");
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException();
        }
    }
}
