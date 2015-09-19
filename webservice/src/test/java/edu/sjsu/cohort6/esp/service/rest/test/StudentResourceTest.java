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

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by rwatsh on 9/17/15.
 *
 * Refer - https://jersey.java.net/documentation/latest/client.html
 *
 * for more on the Jersey client APIs.
 */
public class StudentResourceTest {

    public static final String BASE_URI = "http://localhost:8080";
    private Client client;
    private WebTarget webTarget;
    private static final Logger log = Logger.getLogger(StudentResourceTest.class.getName());

    @BeforeMethod
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI);

    }

    @org.testng.annotations.Test
    public void testGetStudent() throws Exception {
        Response response = webTarget.path("/students")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        logResponse(response);
        Assert.assertTrue(response.getStatus() == 200);
    }

    private void logResponse(Response response) {
        log.info(response.toString());
        log.info(response.readEntity(String.class));
    }

}