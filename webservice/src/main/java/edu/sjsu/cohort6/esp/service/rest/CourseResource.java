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
import edu.sjsu.cohort6.esp.common.Course;
import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.service.rest.exception.BadRequestException;
import edu.sjsu.cohort6.esp.service.rest.exception.InternalErrorException;
import edu.sjsu.cohort6.esp.service.rest.exception.ResourceNotFoundException;
import io.dropwizard.auth.Auth;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rwatsh on 9/24/15.
 */
@Path(EndpointUtils.ENDPOINT_ROOT + "/courses")
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource extends BaseResource<Course> {

    private static final Logger log = Logger.getLogger(Course.class.getName());

    public CourseResource(DBClient client) {
        super(client);
    }

    @Override
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Auth User user, @Valid String courseJson, @Context UriInfo info) {
        try {
            Course c = CommonUtils.convertJsonToObject(courseJson, Course.class);
            List<Course> courseList = new ArrayList<>();
            courseList.add(c);
            courseDAO.add(courseList);
            URI uri = UriBuilder.fromResource(CourseResource.class).build(c.getId());
            return Response.created(uri)
                    .entity(Entity.json(c))
                    .build();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error in adding course", e);
            throw new BadRequestException();
        }

    }

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> list(@Auth User user, @QueryParam("filter") String filter) throws InternalErrorException {
        List<Course> courseList = null;
        if (filter != null && !filter.trim().isEmpty()) {
            String query = null;
            try {
                query = buildQueryJson(filter);
            } catch (UnsupportedEncodingException e) {
                throw new InternalErrorException(e);
            }
            courseList = courseDAO.fetch(query);
        } else {
            courseList = courseDAO.fetchById(null);
        }
        return courseList;
    }

    /**
     * Returns a decoded query string.
     *
     * TODO: We may also build a query as described in:
     * https://americommerce.zendesk.com/hc/en-us/articles/202836800-Resource-Query-Filtering-Syntax
     *
     * Once we do that we can build the query string in JSON to be passed to mongodb.
     *
     * @param query
     * @return
     */
    private String buildQueryJson(String query) throws UnsupportedEncodingException {

        return URLDecoder.decode(query, "UTF-8");
    }

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Course retrieve(@Auth User user, @PathParam("id") String id) throws ResourceNotFoundException, InternalErrorException {
        List<String> courseIdList = getListFromEntityId(id);
        List<Course> courseList = courseDAO.fetchById(courseIdList);
        if (courseList != null && !courseList.isEmpty()) {
            return courseList.get(0);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Course update(@Auth User user, @PathParam("id") String id, @Valid String courseJson) throws ResourceNotFoundException, InternalErrorException, IOException {
        Course entity = CommonUtils.convertJsonToObject(courseJson, Course.class);
        entity.setId(id);
        try {
            courseDAO.update(getListFromEntity(entity));
            List<Course> courseList = courseDAO.fetchById(getListFromEntityId(id));
            if (courseList != null && !courseList.isEmpty()) {
                return courseList.get(0);
            }
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@Auth User user, @PathParam("id") String id) throws ResourceNotFoundException, InternalErrorException {
        try {
            courseDAO.remove(getListFromEntityId(id));
            return Response.ok().build();
        } catch (Exception e) {
            throw new ResourceNotFoundException();
        }
    }
}
