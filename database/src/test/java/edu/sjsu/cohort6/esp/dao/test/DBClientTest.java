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
import edu.sjsu.cohort6.esp.common.Course;
import edu.sjsu.cohort6.esp.common.Student;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.dao.DBFactory;
import edu.sjsu.cohort6.esp.dao.DatabaseModule;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Unit test for DB Client.
 *
 * @author rwatsh
 */
public class DBClientTest {

    @Inject
    DBFactory dbFactory;

    public static final String TESTREG = "testreg";
    private static final Logger log = Logger.getLogger(DBClientTest.class.getName());
    private DBClient client;
    private long startTime;

    @BeforeTest
    public void setUp() throws Exception {
        Module module = new DatabaseModule();
        Guice.createInjector(module).injectMembers(this);
        client = dbFactory.create("localhost", 27017, TESTREG);
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



    @Test
    public void testAddStudents() {
        testCreateStudents();
    }

    private List<String> testCreateStudents() {
        Student s = new Student("Watsh", "Rajneesh", "watsh.rajneesh@sjsu.edu", "password");
        List<Student> studentList = new ArrayList<>();
        studentList.add(s);
        List<String> insertedIds = client.addStudents(studentList);
        List<Student> students = client.fetchStudents(insertedIds);
        Assert.assertNotNull(students);
        log.info("Student created: " + students);
        return insertedIds;
    }

    @Test
    public void testRemoveStudents() throws Exception {
        List<String> insertedIds = testCreateStudents();
        Assert.assertNotNull(insertedIds);
        long countRemovedEntries = client.removeStudents(insertedIds);
        Assert.assertTrue(countRemovedEntries > 0, "Failed to delete any student");
    }

    @Test
    public void testUpdateStudents() throws Exception {
        List<String> insertedIds = testCreateStudents();
        Assert.assertNotNull(insertedIds);
        List<Student> students = client.fetchStudents(insertedIds);
        Assert.assertNotNull(students);
        for (Student s : students) {
            s.setLastName("test");
        }
        log.info("Student modified: " + students);
        client.updateStudents(students);
        students = client.fetchStudents(insertedIds);
        Assert.assertNotNull(students);
        log.info("Student updated in DB: " + students);
    }

    @Test
    public void testEnrollStudents() throws Exception {
        List<String> insertedIds = testCreateStudents();
        List<String> courseIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        Assert.assertNotNull(courseIds);
        List<Student> students = client.fetchStudents(insertedIds);
        List<Course> courses = client.fetchCourses(courseIds);
        Assert.assertNotNull(students);
        Assert.assertNotNull(courses);
        for (Student s : students) {
            s.getCourseRefs().addAll(courses);
        }
        log.info("Student modified: " + students);
        client.updateStudents(students);
        students = client.fetchStudents(insertedIds);
        Assert.assertNotNull(students);
        log.info("Student updated in DB: " + students);
    }

    @Test
    public void testFetchStudents() throws Exception {
        List<String> insertedIds = testCreateStudents();
        Assert.assertNotNull(insertedIds);
        List<Student> students = client.fetchStudents(insertedIds);
        Assert.assertNotNull(students);
    }

    @Test
    public void testAddCourse() throws ParseException {
        testCreateCourse();
    }

    private List<String> testCreateCourse() throws ParseException {
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
                .startTime(getDateFromString("10-10-2015 10:30"))
                .endTime(getDateFromString("11-10-2015 13:00"))
                .instructors(instructors)
                .location("Santa Clara, CA")
                .keywords(keywords)
                .build();
        log.info("Course : " + course);
        List<String> insertedIds = client.addCourse(new ArrayList<Course>() {{
            add(course);
        }});

        List<Course> courses = client.fetchCourses(insertedIds);
        Assert.assertNotNull(courses);
        log.info("Course created: " + courses);
        return insertedIds;
    }



    private Date getDateFromString(String dateInString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy HH:mm");
        return sdf.parse(dateInString);
    }

    private Date getTimeFromString(String timeInString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.parse(timeInString);
    }

    @Test
    public void testRemoveCourses() throws Exception {
        List<String> insertedIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        long countRemovedEntries = client.removeCourses(insertedIds);
        Assert.assertTrue(countRemovedEntries > 0, "Failed to delete any course");
    }

    @Test
    public void testUpdateCourses() throws Exception {
        List<String> insertedIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        List<Course> courses = client.fetchCourses(insertedIds);
        Assert.assertNotNull(courses);
        for (Course c : courses) {
            c.setAvailabilityStatus(Course.AvailabilityStatus.UNAVAILABLE.getValue());
        }
        log.info("Course modified: " + courses);
        client.updateCourses(courses);
        courses = client.fetchCourses(insertedIds);
        Assert.assertNotNull(courses);
        log.info("Course updated in DB: " + courses);
    }

    @Test
    public void testFetchCourses() throws Exception {
        List<String> insertedIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        List<Course> courses = client.fetchCourses(insertedIds);
        Assert.assertNotNull(courses);
    }
}