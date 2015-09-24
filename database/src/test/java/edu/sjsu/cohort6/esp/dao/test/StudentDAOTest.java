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
import edu.sjsu.cohort6.esp.common.Student;
import edu.sjsu.cohort6.esp.dao.mongodb.CourseDAO;
import edu.sjsu.cohort6.esp.dao.mongodb.StudentDAO;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Student DAO tests.
 *
 * @author rwatsh on 9/24/15.
 */
public class StudentDAOTest extends DBTest<StudentDAO, Student> {
    private final static Logger log = Logger.getLogger(DBTest.class.getName());


    private List<String> testCreateStudents()  {
        Student s = new Student(testCreateUser());
        List<Student> studentList = new ArrayList<>();
        studentList.add(s);
        List<String> insertedIds = dao.add(studentList);
        List<Student> students = dao.fetchById(insertedIds);
        Assert.assertNotNull(students);
        log.info("Student created: " + students);
        return insertedIds;
    }

    @Override
    public void testAdd(List<Student> entityList) {
        testCreateStudents();
    }

    @Override
    public void testRemove() throws Exception {
        List<String> insertedIds = testCreateStudents();
        Assert.assertNotNull(insertedIds);
        long countRemovedEntries = dao.remove(insertedIds);
        Assert.assertTrue(countRemovedEntries > 0, "Failed to delete any student");
    }

    @Override
    public void testUpdate() throws Exception {
        List<String> insertedIds = testCreateStudents();
        Assert.assertNotNull(insertedIds);
        List<Student> students = dao.fetchById(insertedIds);
        Assert.assertNotNull(students);
        for (Student s : students) {
            s.getUser().setLastName("test");
        }
        log.info("Student's user modified: " + students);
        dao.update(students);
        students = dao.fetchById(insertedIds);
        Assert.assertNotNull(students);
        log.info("Student updated in DB: " + students);
    }



    @Override
    public void testFetch() throws Exception {
        List<String> insertedIds = testCreateStudents();
        Assert.assertNotNull(insertedIds);
        List<Student> students = dao.fetchById(insertedIds);
        Assert.assertNotNull(students);
    }

    // Additional student specific test methods.

    @Test
    public void testFetchStudentsNull() {
        testCreateStudents();
        List<Student> students = dao.fetchById(null);
        Assert.assertNotNull(students);
    }

    @Test
    public void testEnrollStudents() throws Exception {
        List<String> insertedIds = testCreateStudents();
        List<String> courseIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        Assert.assertNotNull(courseIds);
        List<Student> students = dao.fetchById(insertedIds);
        List<Course> courses = ((CourseDAO)client.getDAO(CourseDAO.class)).fetchById(courseIds);
        Assert.assertNotNull(students);
        Assert.assertNotNull(courses);
        for (Student s : students) {
            s.getCourseRefs().addAll(courses);
        }
        log.info("Student modified: " + students);
        dao.update(students);
        students = dao.fetchById(insertedIds);
        Assert.assertNotNull(students);
        Assert.assertTrue(!students.get(0).getCourseRefs().isEmpty());
        log.info("Student updated in DB: " + students);
    }

}
