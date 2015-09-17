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

package edu.sjsu.cohort6.esp.dao;


import edu.sjsu.cohort6.esp.common.Course;
import edu.sjsu.cohort6.esp.common.Student;

import java.util.List;

/**
 * A generic DB Client interface.
 *
 * @author rwatsh
 */
public interface DBClient extends AutoCloseable {
    // Common
    void dropDB(String dbName);
    void useDB(String dbName);

    // Student
    List<String> addStudents(List<Student> studentList);
    long removeStudents(List<String> studentIdsList);
    void updateStudents(List<Student> studentList);
    void updateStudents(List<Student> studentList, List<Course> courseList);
    List<Student> fetchStudents(List<String> studentIdsList);

    // Course
    List<String> addCourse(List<Course> courseList);
    long removeCourses(List<String> courseIdsList);
    void updateCourses(List<Course> courseList);
    List<Course> fetchCourses(List<String> courseIdsList);
}
