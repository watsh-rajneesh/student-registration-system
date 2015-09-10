package edu.sjsu.cohort6.esp.dao;


import edu.sjsu.cohort6.esp.dao.model.Course;
import edu.sjsu.cohort6.esp.dao.model.Student;

import java.util.List;

/**
 * Created by rwatsh on 9/8/15.
 */
public interface DBClient extends AutoCloseable {
    // Common
    void dropDB(String dbName);
    void useDB(String dbName);

    // Student
    void addStudents(List<Student> studentList);
    long removeStudents(List<String> studentIdsList);
    void updateStudents(List<Student> studentList);
    List<Student> fetchStudents(List<String> studentIdsList);

    // Course
    void addCourse(List<Course> courseList);
    long removeCourses(List<String> courseIdsList);
    void updateCourses(List<Course> courseList);
    List<Course> fetchCourses(List<String> courseIdsList);
}
