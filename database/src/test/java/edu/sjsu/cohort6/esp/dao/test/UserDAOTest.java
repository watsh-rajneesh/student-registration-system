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

package edu.sjsu.cohort6.esp.dao.test;

import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.dao.mongodb.UserDAO;
import org.testng.Assert;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author rwatsh on 9/24/15.
 */
public class UserDAOTest extends DBTest<UserDAO, User> {

    private static final Logger log = Logger.getLogger(UserDAOTest.class.getName());

    @Override
    public void testAdd() {
        testCreateUser();
    }

    @Override
    public void testRemove() throws Exception {

    }

    @Override
    public void testUpdate() throws Exception {

    }

    @Override
    public void testFetch() throws Exception {
        testCreateUser();
        List<User> users = dao.fetchById(null);
        log.info(users.toString());
        Assert.assertNotNull(users);
        Assert.assertTrue(!users.isEmpty(), "Could not fetch users");
    }
}
