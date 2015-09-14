package edu.sjsu.cohort6.esp.dao;

import edu.sjsu.cohort6.esp.dao.mongodb.Course;
import edu.sjsu.cohort6.esp.dao.mongodb.Student;

import java.util.List;

/**
 * Created by rwatsh on 9/13/15.
 */
public class MockDBClient implements DBClient {
    @Override
    public void dropDB(String dbName) {

    }

    @Override
    public void useDB(String dbName) {

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
    public void addCourse(List<Course> courseList) {

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
