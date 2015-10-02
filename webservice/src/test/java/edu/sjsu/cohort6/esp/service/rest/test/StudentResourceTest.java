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

package edu.sjsu.cohort6.esp.service.rest.test;

import edu.sjsu.cohort6.esp.common.CommonUtils;
import edu.sjsu.cohort6.esp.common.Course;
import edu.sjsu.cohort6.esp.common.Student;
import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.db.test.DBTest;
import edu.sjsu.cohort6.esp.service.rest.EndpointUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by rwatsh on 9/17/15.
 * <p>
 * Refer - https://jersey.java.net/documentation/latest/client.html
 * <p>
 * for more on the Jersey client APIs.
 */
public class StudentResourceTest extends BaseResourceTest {

    public static final String STUDENT_RESOURCE_URI = EndpointUtils.ENDPOINT_ROOT + "/students";
    private static final Logger log = Logger.getLogger(StudentResourceTest.class.getName());

    @Test
    public void testFetchAll() throws Exception {
        List<Student> studentList  = getStudents();
        Assert.assertNotNull(studentList);
    }

    private List<Student> getStudents() throws Exception {
        Response response = webTarget.path(STUDENT_RESOURCE_URI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        log.info(response.toString());
        String respStr = response.readEntity(String.class);
        List<Student> studentList = CommonUtils.convertJsonArrayToList(respStr, Student.class);
        log.info(studentList.toString());
        Assert.assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
        return studentList;
    }

    @Test
    public void testFetch() throws Exception {
        List<Student> studentList = getStudents();
        if (studentList != null && !studentList.isEmpty()) {
            String id = studentList.get(0).getId();

            Response response = webTarget.path(STUDENT_RESOURCE_URI)
                    .queryParam("id", id)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            log.info(response.toString());
            String respStr = response.readEntity(String.class);
            studentList = CommonUtils.convertJsonArrayToList(respStr, Student.class);
            log.info(studentList.toString());
            Assert.assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
        }

    }


    @Test
    public void testAdd() throws Exception {
        Student s = createStudent();
        Assert.assertNotNull(s);
    }

    @Override
    public void testRemove() throws Exception {

    }

    @Override
    public void testUpdate() throws Exception {

    }

    private Student createStudent() throws Exception {
        Student s = new Student();
        User user = DBTest.getTestUser();
        s.setUser(user);
        Course c = DBTest.getTestCourse();
        List<Course> courses = new ArrayList<>();
        courses.add(c);

        s.setCourseRefs(courses);

        // Convert to string
        String studentJson = CommonUtils.convertObjectToJson(s);
        log.info(studentJson);

        // Use json string to web service.

        Invocation.Builder invocationBuilder = webTarget.path(STUDENT_RESOURCE_URI)
                .request().accept(MediaType.APPLICATION_JSON_TYPE).header("content-type", MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(Entity.json(studentJson));

        log.info(response.toString());
        //String studentStr = response.readEntity(String.class);

        Student student = CommonUtils.convertJsonToObject(response, Student.class);
        log.info(s.toString());
        Assert.assertTrue(response.getStatus() == Response.Status.CREATED.getStatusCode());
        return s;
    }



}