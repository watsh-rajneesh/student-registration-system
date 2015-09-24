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
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Course DAO tests.
 *
 * @author rwatsh on 9/24/15.
 */
public class CourseDAOTest extends DBTest<CourseDAO> {
    private static final Logger log = Logger.getLogger(CourseDAOTest.class.getName());

    @Test
    public void testAddCourse() throws ParseException {
        testCreateCourse();
    }

    @Test
    public void testRemoveCourses() throws Exception {
        List<String> insertedIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        long countRemovedEntries = dao.remove(insertedIds);
        Assert.assertTrue(countRemovedEntries > 0, "Failed to delete any course");
    }

    @Test
    public void testUpdateCourses() throws Exception {
        List<String> insertedIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        List<Course> courses = dao.fetch(insertedIds);
        Assert.assertNotNull(courses);
        for (Course c : courses) {
            c.setAvailabilityStatus(Course.AvailabilityStatus.UNAVAILABLE.getValue());
        }
        log.info("Course modified: " + courses);
        dao.update(courses);
        courses = dao.fetch(insertedIds);
        Assert.assertNotNull(courses);
        log.info("Course updated in DB: " + courses);
    }

    @Test
    public void testFetchCourses() throws Exception {
        List<String> insertedIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        List<Course> courses = dao.fetch(insertedIds);
        Assert.assertNotNull(courses);
    }
}
