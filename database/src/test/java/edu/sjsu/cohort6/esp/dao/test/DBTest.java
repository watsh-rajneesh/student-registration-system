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

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import edu.sjsu.cohort6.esp.common.*;
import edu.sjsu.cohort6.esp.dao.BaseDAO;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.dao.DBFactory;
import edu.sjsu.cohort6.esp.dao.DatabaseModule;
import edu.sjsu.cohort6.esp.dao.mongodb.CourseDAO;
import edu.sjsu.cohort6.esp.dao.mongodb.UserDAO;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class is meant to be inherited from by all DB tests.
 * It has methods for test setup and tear down.
 *
 * @author rwatsh on 9/24/15.
 */
public abstract class DBTest<T extends BaseDAO, S> {
    @Inject
    private DBFactory dbFactory;
    protected T dao;
    private Class<T> tClass;

    public static final String TESTREG = "testreg";
    private static final Logger log = Logger.getLogger(DBTest.class.getName());
    protected DBClient client;
    private long startTime;

    public DBTest() {
        Module module = new DatabaseModule();
        Guice.createInjector(module).injectMembers(this);
        client = dbFactory.create("localhost", 27017, TESTREG);
        /*
         * Use reflection to infer the class for T type.
         */
        this.tClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        dao = (T) client.getDAO(tClass);
    }

    @BeforeTest
    public void setUp() throws Exception {

        client.dropDB(TESTREG);
    }


    @AfterTest
    public void tearDown() throws Exception {
        client.close();
    }

    @BeforeMethod
    public void createDB() {
        client.useDB(TESTREG);
        log.info("********************");
        startTime = System.currentTimeMillis();
    }

    @AfterMethod
    public void dropDB() {
        //client.dropDB(TESTREG);
        long endTime = System.currentTimeMillis();
        long diff = endTime - startTime;
        log.info(MessageFormat.format("********* Time taken: {0} ms", diff));
    }

    /*
     * Abstract test methods to be implemented by concrete test classes.
     */
    @Test
    abstract public void testAdd(List<S> entityList) throws Exception;

    @Test
    abstract public void testRemove() throws Exception;

    @Test
    abstract public void testUpdate() throws Exception;

    @Test
    abstract public void testFetch() throws Exception;

    /**
     * Common test methods shared across test sub classes.
     */


    protected User testCreateUser() {
        User user = new User("watsh.rajneesh@sjsu.edu", "watsh.rajneesh@sjsu.edu", "Watsh", "Rajneesh", new Role(RoleType.STUDENT));
        UserDAO userDAO = (UserDAO)client.getDAO(UserDAO.class);
        List<User> usersList = new ArrayList<>();
        usersList.add(user);
        List<String> insertedIds = userDAO.add(usersList);
        log.info("User: " + user);
        List<User> users = userDAO.fetchById(insertedIds);
        Assert.assertNotNull(users);
        log.info("User created: " + users);
        return users.get(0);
    }

    /**
     * This method is common code between student and course tests so kept in the base class.
     *
     * @return
     * @throws ParseException
     */
    protected List<String> testCreateCourse() throws ParseException {
        ArrayList<String> keywords = new ArrayList<String>() {{
            add("Java");
            add("MongoDB");
            add("REST");
        }};
        ArrayList<String> instructors = new ArrayList<String>() {{
            add("Ahmad Nouri");
            add("Thomas Hildebrand");
            add("Aktouf");
        }};
        Course course = new Course.Builder("Cloud Technologies")
                .maxCapacity(20)
                .price(200.0)
                .availabilityStatus(Course.AvailabilityStatus.AVAILABLE.getValue())
                .startTime(CommonUtils.getDateFromString("10-10-2015 10:30"))
                .endTime(CommonUtils.getDateFromString("11-10-2015 13:00"))
                .instructors(instructors)
                .location("Santa Clara, CA")
                .keywords(keywords)
                .build();
        log.info("Course : " + course);
        CourseDAO courseDAO = (CourseDAO)client.getDAO(CourseDAO.class);
        List<String> insertedIds = courseDAO.add(new ArrayList<Course>() {{
            add(course);
        }});

        List<Course> courses = courseDAO.fetchById(insertedIds);
        Assert.assertNotNull(courses);
        log.info("Course created: " + courses);
        return insertedIds;
    }
}
