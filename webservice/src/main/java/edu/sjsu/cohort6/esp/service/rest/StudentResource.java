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

import edu.sjsu.cohort6.esp.common.*;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.service.rest.exception.AuthorizationException;
import edu.sjsu.cohort6.esp.service.rest.exception.BadRequestException;
import edu.sjsu.cohort6.esp.service.rest.exception.InternalErrorException;
import edu.sjsu.cohort6.esp.service.rest.exception.ResourceNotFoundException;
import io.dropwizard.auth.Auth;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
    public Response create(@Auth(required=false) User user, @Valid String studentJson, @Context UriInfo info) {
        try {
            Student s = CommonUtils.convertJsonToObject(studentJson, Student.class);
            List<Student> studentList = new ArrayList<>();
            studentList.add(s);
            createUserForStudent(s);
            findCoursesForStudent(s);
            studentDAO.add(studentList);
            URI uri = UriBuilder.fromResource(StudentResource.class).build(s.getId());
            return Response.created(uri)
                        .entity(Entity.json(s))
                        .build();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error in adding student", e);
            throw new BadRequestException();
        }
    }

    public void findCoursesForStudent(Student s) {
        List<Course> courses = s.getCourseRefs();
        List<Course> coursesFoundList = new ArrayList<>();
        if (courses != null && !courses.isEmpty()) {
            // find course by name
            for (Course course : courses) {
                Course c =  courseDAO.fetchCourseByName(course.getCourseName());
                if (c != null) {
                    coursesFoundList.add(c);
                }
            }
        }
        s.setCourseRefs(coursesFoundList);
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
    public List list(@Auth User user, @QueryParam("filter") String filter) {
        /**
         * This method can only be run by an ADMIN.
         */
        if (user.getRole().getRole().equals(RoleType.ADMIN)) {
            List<String> studentIds = new ArrayList<>();
            List<Student> studentList = studentDAO.fetchById(studentIds);
            return studentList;
        } else {
            throw new AuthorizationException("User " + user.getUserName() + " is not allowed to perform this operation");
        }
    }

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Student retrieve(@Auth User user, @PathParam("id") String studentId) throws ResourceNotFoundException {
        List<String> studentIds = getListFromEntityId(studentId);
        List<Student> studentList = studentDAO.fetchById(studentIds);
        if (studentList != null && !studentList.isEmpty()) {
            return studentList.get(0);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Student update(@Auth User user, @PathParam("id") String id, @Valid String studentJson) throws ResourceNotFoundException, IOException {
        /*Student student = CommonUtils.convertJsonToObject(studentJson, Student.class);
        student.setId(id);*/
        try {
            Student student = null;
            List<Student> studentList = studentDAO.fetchById(getListFromEntityId(id));
            if (studentList != null && !studentList.isEmpty()) {
                student = studentList.get(0);
            }
            if (student == null) {
                throw new ResourceNotFoundException();
            }

            JSONParser parser=new JSONParser();
            JSONObject json = (JSONObject) parser.parse(studentJson);
            JSONObject userObj = (JSONObject) json.get("user");

            if (userObj != null) {
                String val = (String) userObj.get("emailId");
                if (val != null) {
                    student.getUser().setEmailId(val);
                }
                val = (String) userObj.get("userName");
                if (val != null) {
                    student.getUser().setUserName(val);
                }
                val = (String) userObj.get("token");
                if (val != null) {
                    student.getUser().setToken(val);
                }
                val = (String) userObj.get("firstName");
                if (val != null) {
                    student.getUser().setFirstName(val);
                }
                val = (String) userObj.get("lastName");
                if (val != null) {
                    student.getUser().setLastName(val);
                }
                JSONObject roleObj = (JSONObject) userObj.get("role");
                if (roleObj != null) {
                    val = (String) roleObj.get("role");
                    if (val != null) {
                        student.getUser().getRole().setRole(val.equalsIgnoreCase("student") ?
                        RoleType.STUDENT : RoleType.ADMIN);
                    }
                }
            }
            JSONObject courseObj = (JSONObject) json.get("courseRef");
            if (courseObj != null) {
                String val = (String) courseObj.get("enrollId");
                if (val != null) {
                    List<Course> courses = courseDAO.fetchById(getListFromEntityId(val));
                    enrollToCourse(student, courses);
                } else {
                    val = (String) courseObj.get("enrollCourseName");
                    if (val != null) {
                        List<Course> courses = courseDAO.fetch("{courseName: \"" + val + "\"}");
                        enrollToCourse(student, courses);
                    }
                }

                val = (String) courseObj.get("unEnrollId");
                if (val != null) {
                    List<Course> courses = courseDAO.fetchById(getListFromEntityId(val));
                    if (!courses.isEmpty()) {
                        student.getCourseRefs().remove(courses.get(0));
                    }
                } else {
                    val = (String) courseObj.get("unEnrollCourseName");
                    if (val != null) {
                        List<Course> courses = courseDAO.fetch("{courseName: \"" + val + "\"}");
                        if (!courses.isEmpty()) {
                            student.getCourseRefs().remove(courses.get(0));
                        }
                    }
                }
            }

            studentDAO.update(getListFromEntity(student));
            return student;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
    }

    /**
     * Adds the course to student only if it is not already present.
     *
     * @param student
     * @param courses
     */
    private void enrollToCourse(Student student, List<Course> courses) {
        if (!courses.isEmpty()) {
            Course course = courses.get(0);
            if (!student.getCourseRefs().contains(course)) {
                student.getCourseRefs().add(course);
            }

        }
    }

    @Override
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@Auth User user, @PathParam("id") String id) throws ResourceNotFoundException {
        try {
            studentDAO.remove(getListFromEntityId(id));
            return Response.ok().build();
        } catch (Exception e) {
            throw new ResourceNotFoundException();
        }
    }
}
