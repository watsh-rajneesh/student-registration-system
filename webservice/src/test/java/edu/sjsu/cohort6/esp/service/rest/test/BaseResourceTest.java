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

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * @author rwatsh on 10/1/15.
 */
public abstract class BaseResourceTest {

    protected static final String BASE_URI = "http://localhost:8080";
    protected Client client;
    protected WebTarget webTarget;

    @BeforeClass
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
        /*HttpAuthenticationFeature feature = HttpAuthenticationFeature
                .basic("nilay.kothari@sjsu.edu", "56cdf9a7-0f75-490f-88cf-03d7f57cb9b8");
        client.register(feature);*/
        webTarget = client.target(BASE_URI);
    }

    @AfterClass
    public void tearDown() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    /*
     * Abstract test methods to be implemented by concrete test classes.
     */
    @Test
    abstract public void testAdd() throws Exception;

    @Test
    abstract public void testRemove() throws Exception;

    @Test
    abstract public void testUpdate() throws Exception;

    @Test
    abstract public void testFetch() throws Exception;

    @Test
    abstract public void testFetchAll() throws Exception;

}
