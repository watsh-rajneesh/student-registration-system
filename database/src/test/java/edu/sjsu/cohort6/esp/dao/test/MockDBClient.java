package edu.sjsu.cohort6.esp.dao.test;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import edu.sjsu.cohort6.esp.dao.test.mongodb.Course;
import edu.sjsu.cohort6.esp.dao.test.mongodb.Student;

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
