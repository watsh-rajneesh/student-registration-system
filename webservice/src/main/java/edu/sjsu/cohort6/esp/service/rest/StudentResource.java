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
import edu.sjsu.cohort6.esp.common.Student;
import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.service.rest.exception.BadRequestException;
import edu.sjsu.cohort6.esp.service.rest.exception.InternalErrorException;
import edu.sjsu.cohort6.esp.service.rest.exception.ResourceNotFoundException;
import io.dropwizard.auth.Auth;
import org.bson.types.ObjectId;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rwatsh on 9/15/15.
 */
@Path(EndpointUtils.ENDPOINT_ROOT + "/students")
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource extends BaseResource<Student> {

    private static final Logger log = Logger.getLogger(StudentResource.class.getName());




    public StudentResource(DBClient client) {
        super(client);
    }


    @Override
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Auth User user, @Valid String studentJson, @Context UriInfo info) {
        try {
            Student s = CommonUtils.convertJsonToObject(studentJson, Student.class);
            List<Student> studentList = new ArrayList<>();
            studentList.add(s);
            createUserForStudent(s);
            findCoursesForStudent(s);
            studentDAO.add(studentList);
                return Response.ok()
                        .entity(Entity.json(s))
                        .build();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error in adding student", e);
            throw new BadRequestException();
        }
    }

    public void findCoursesForStudent(Student s) {
        List<Course> courses = s.getCourseRefs();
        if (courses != null && !courses.isEmpty()) {
            // find course by name
            for (Course course : courses) {
                Course c = courseDAO.fetchCourseByName(course.getCourseName());
                course.set_id(c.get_id());
            }
        }
    }

    public void createUserForStudent(Student s) {
        User u = s.getUser();
        List<User> userList = new ArrayList<>();
        userList.add(u);
        userDAO.add(userList);
    }

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List list(@Auth User user) {
        List<String> studentIds = new ArrayList<>();
        List<Student> studentList = studentDAO.fetchById(studentIds);
        return studentList;
    }

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Student retrieve(@Auth User user, @PathParam("id") String studentId) throws ResourceNotFoundException {
        List<String> studentIds = getStudentIdsList(studentId);
        List<Student> studentList = studentDAO.fetchById(studentIds);
        if (studentList != null && !studentList.isEmpty()) {
            return studentList.get(0);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private List<String> getStudentIdsList(String studentId) {
        List<String> studentIds = new ArrayList<>();

        if (studentId != null && !studentId.isEmpty()) {
            studentIds.add(studentId);
        } else {
            studentIds = null;
        }
        return studentIds;
    }

    private List<Student> getStudentsList(Student student) {
        List<Student> students = new ArrayList<>();

        if (student != null) {
            students.add(student);
        } else {
            students = null;
        }
        return students;
    }

    @Override
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Student update(@Auth User user, @PathParam("id") String id, @Valid Student student) throws ResourceNotFoundException {
        student.set_id(new ObjectId(id));
        try {
            studentDAO.update(getStudentsList(student));
            List<Student> studentList = studentDAO.fetchById(getStudentIdsList(id));
            if (studentList != null && !studentList.isEmpty()) {
                return studentList.get(0);
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
    public Response delete(@Auth User user, @PathParam("id") String id) throws ResourceNotFoundException {
        try {
            studentDAO.remove(getStudentIdsList(id));
            return Response.ok().build();
        } catch (Exception e) {
            throw new ResourceNotFoundException();
        }
    }
}
