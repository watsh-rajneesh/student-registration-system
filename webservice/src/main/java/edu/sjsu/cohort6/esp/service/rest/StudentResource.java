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

import com.google.common.base.Optional;
import edu.sjsu.cohort6.esp.common.Student;
import edu.sjsu.cohort6.esp.dao.DBClient;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Created by rwatsh on 9/15/15.
 */
@Path(EndpointUtils.ENDPOINT_ROOT + "/students")
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource {
    private DBClient dbClient;

    public StudentResource(DBClient client) {
        this.dbClient = client;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> fetch(@QueryParam("id") Optional<String> studentId) {
        List<String> studentIds = new ArrayList<>();
        if (studentId != null && !studentId.equals(Optional.<String>absent())) {
            studentIds.add(studentId.get());
        } else {
            studentIds = null;
        }
        List<Student> studentList = dbClient.fetchStudents(studentIds);
        if (studentList != null) {
            return studentList;
            //return Response.ok(studentList).build();
        } else {
            throw new WebApplicationException(NOT_FOUND);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createStudent(@Valid Student student) throws Exception {
        try {
            List<Student> studentList = new ArrayList<>();
            studentList.add(student);
            List<String> studentIds = dbClient.addStudents(studentList);
            return Response.created(UriBuilder.fromResource(getClass())
                    .build(student, studentIds.get(0)))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(BAD_REQUEST);
        }
    }

}
