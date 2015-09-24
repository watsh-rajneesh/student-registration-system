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

import edu.sjsu.cohort6.esp.common.Course;
import edu.sjsu.cohort6.esp.dao.mongodb.CourseDAO;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Course DAO tests.
 *
 * @author rwatsh on 9/24/15.
 */
public class CourseDAOTest extends DBTest<CourseDAO, Course> {
    private static final Logger log = Logger.getLogger(CourseDAOTest.class.getName());

    @Override
    public void testAdd() throws Exception {
        testCreateCourse();
    }

    @Override
    public void testRemove() throws Exception {
        List<String> insertedIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        long countRemovedEntries = dao.remove(insertedIds);
        Assert.assertTrue(countRemovedEntries > 0, "Failed to delete any course");
    }

    @Override
    public void testUpdate() throws Exception {
        List<String> insertedIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        List<Course> courses = dao.fetchById(insertedIds);
        Assert.assertNotNull(courses);
        for (Course c : courses) {
            c.setAvailabilityStatus(Course.AvailabilityStatus.UNAVAILABLE.getValue());
        }
        log.info("Course modified: " + courses);
        dao.update(courses);
        courses = dao.fetchById(insertedIds);
        Assert.assertNotNull(courses);
        log.info("Course updated in DB: " + courses);
    }

    @Override
    public void testFetch() throws Exception {
        List<String> insertedIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        List<Course> courses = dao.fetchById(insertedIds);
        Assert.assertNotNull(courses);
    }

    // Additional course specific tests.

    /**
     * Complex queries test.
     *
     * @param m
     * @return
     * @see http://docs.mongodb.org/manual/reference/sql-comparison/
     * @see http://mongodb.github.io/morphia/1.1/guides/querying/
     */
    @DataProvider(name = "dp")
    public Object[][] createData(Method m) {
        System.out.println(m.getName());  // print test method name
            return new Object[][]{
                    {"{ price: { $gte: 100}}", 1},
                    {"{ price: { $gte: 100}, keywords: \"REST\", keywords: \"Java\" }", 1},
                    {"{ price: { $gte: 100}, keywords: \"REST\", keywords: \"Java1\" }", 0},
                    {"{ location: \"Santa Clara, CA\"}", 1},
                    {"{ $or: [ { location: \"Santa Clara, CA\" } ,  { price: {$lt: 500} } ] }", 1}
            };

        }
    @Test(dataProvider = "dp")
    public void testFetchParametrized(String query, int expectedCount) throws ParseException {
        testCreateCourse();
        List<Course> courses = dao.fetch(query);
        Assert.assertTrue(courses.size() >= expectedCount,
                MessageFormat.format("Test failed for query: {0}, found records [{1}], expected count {2}", query, courses.size(), expectedCount));
    }
}
