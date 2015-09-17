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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rwatsh on 9/15/15.
 */
@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource {

    @GET
    public List<Student> getStudent(@QueryParam("id") Optional<String> studentId) {
        Student s = new Student("Watsh", "Rajneesh", "watsh.rajneesh@sjsu.edu", "password");

        ArrayList<Student> students = new ArrayList<>();

        students.add(s);
        return students;
    }
}
