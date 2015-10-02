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
import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.db.test.DBTest;
import edu.sjsu.cohort6.esp.service.rest.EndpointUtils;
import org.testng.Assert;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * @author rwatsh on 10/1/15.
 */
public class UserResourceTest extends BaseResourceTest {

    public static final String RESOURCE_URI = EndpointUtils.ENDPOINT_ROOT + "/users";
    private static final Logger log = Logger.getLogger(StudentResourceTest.class.getName());

    private User createUser(User user) throws Exception {
        // Convert to string
        String jsonStr = CommonUtils.convertObjectToJson(user);
        log.info(jsonStr);
        Response response = webTarget.path(RESOURCE_URI)
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("content-type", MediaType.APPLICATION_JSON)
                .post(Entity.json(jsonStr));
        //String us = response.readEntity(String.class);
        User u = CommonUtils.convertJsonToObject(response, User.class);
        log.info(u.toString());
        Assert.assertTrue(response.getStatus() == Response.Status.CREATED.getStatusCode());
        return u;
    }

    @Override
    public void testAdd() throws Exception {
        User user = createUser(DBTest.getTestUser());
        Assert.assertNotNull(user);
    }

    @Override
    public void testRemove() throws Exception {
        User user = createUser(DBTest.getTestUser());
        log.info("Created user: " + user);
        Response response = webTarget.path(RESOURCE_URI)
                .path("/" + user.getId())
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("content-type", MediaType.APPLICATION_JSON)
                .delete();
        Assert.assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
        log.info("User deleted: " + user);
    }

    @Override
    public void testUpdate() throws Exception {

    }

    @Override
    public void testFetch() throws Exception {

    }

    @Override
    public void testFetchAll() throws Exception {

    }
}
