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
import edu.sjsu.cohort6.esp.common.Student;
import edu.sjsu.cohort6.esp.service.rest.EndpointUtils;
import org.bson.types.ObjectId;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by rwatsh on 9/17/15.
 * <p>
 * Refer - https://jersey.java.net/documentation/latest/client.html
 * <p>
 * for more on the Jersey client APIs.
 */
public class StudentResourceTest {

    public static final String BASE_URI = "http://localhost:8080";
    public static final String STUDENT_RESOURCE_URI = EndpointUtils.ENDPOINT_ROOT + "/students";
    private Client client;
    private WebTarget webTarget;
    private static final Logger log = Logger.getLogger(StudentResourceTest.class.getName());

    @BeforeClass
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI);
        HttpAuthenticationFeature feature = HttpAuthenticationFeature
                .basic("watsh.rajneesh@sjsu.edu", "6a00b426-1243-4d80-a059-a1973e3482fe");
        client.register(feature);
    }

    @AfterClass
    public void tearDown() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    @Test
    public void testGetStudents() throws Exception {
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
    public void testGetStudent() throws Exception {
        List<Student> studentList = getStudents();
        if (studentList != null && !studentList.isEmpty()) {
            ObjectId id = studentList.get(0).get_id();

            Response response = webTarget.path(STUDENT_RESOURCE_URI)
                    .queryParam("id", id.toHexString())
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
    public void testCreateStudent() throws Exception {
        Student s = createStudent();
        Assert.assertNotNull(s);
    }

    private Student createStudent() throws Exception {
        Student s = new Student();
        Invocation.Builder invocationBuilder = webTarget.path(STUDENT_RESOURCE_URI)
                .request().accept(MediaType.APPLICATION_JSON_TYPE);

        Response response = invocationBuilder.post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE));

        log.info(response.toString());
        s = response.readEntity(Student.class);
        //Student student = CommonUtils.convertJsonToObject(respStr, Student.class);
        log.info(s.toString());
        Assert.assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
        return s;
    }

}