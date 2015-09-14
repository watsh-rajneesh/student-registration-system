package edu.sjsu.cohort6.esp.dao;


import edu.sjsu.cohort6.esp.dao.mongodb.Course;
import edu.sjsu.cohort6.esp.dao.mongodb.Student;

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
    void addCourse(List<Course> courseList);
    long removeCourses(List<String> courseIdsList);
    void updateCourses(List<Course> courseList);
    List<Course> fetchCourses(List<String> courseIdsList);
}
