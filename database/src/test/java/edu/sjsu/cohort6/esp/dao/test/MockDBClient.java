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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.common.Course;
import edu.sjsu.cohort6.esp.common.Student;

import java.util.List;

/**
 * Created by rwatsh on 9/13/15.
 */
public class MockDBClient implements DBClient {

    @Inject
    private MockDBClient(@Assisted("server") String server, @Assisted("port") int port, @Assisted String dbName) {

    }

    @Override
    public void dropDB(String dbName) {

    }

    @Override
    public void useDB(String dbName) {

    }

    @Override
    public boolean checkHealth() {
        return false;
    }

    @Override
    public String getConnectString() {
        return null;
    }

    @Override
    public List<String> addStudents(List<Student> studentList) {

        return null;
    }

    @Override
    public long removeStudents(List<String> studentIdsList) {
        return 0;
    }

    @Override
    public void updateStudents(List<Student> studentList) {

    }

    @Override
    public void updateStudents(List<Student> studentList, List<Course> courseList) {

    }

    @Override
    public List<Student> fetchStudents(List<String> studentIdsList) {
        return null;
    }

    @Override
    public List<String> addCourse(List<Course> courseList) {

        return null;
    }

    @Override
    public long removeCourses(List<String> courseIdsList) {
        return 0;
    }

    @Override
    public void updateCourses(List<Course> courseList) {

    }

    @Override
    public List<Course> fetchCourses(List<String> courseIdsList) {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
