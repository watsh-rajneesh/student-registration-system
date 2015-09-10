package edu.sjsu.cohort6.esp.dao.mongodb;

import com.mongodb.MongoClient;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.dao.model.Course;
import edu.sjsu.cohort6.esp.dao.model.Student;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.List;

/**
 * Created by rwatsh on 9/9/15.
 */
public class MongoDBClient implements DBClient {

    public static void main(String[] args) {
        final Morphia morphia = new Morphia();
        morphia.mapPackageFromClass(Student.class);
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 27017), "esp");
        datastore.ensureIndexes();

        final Student student = new Student("Watsh", "Rajneesh", "watsh.rajneesh@sjsu.edu", "password");
        datastore.save(student);

        final Course course = new Course.Builder("Cloud Technologies")
                .maxCapacity(20)
                .price(200.0)
                .build();
        datastore.save(course);

        student.getCourseRefs().add(course);
        datastore.save(student);

        final Query<Student> query = datastore.createQuery(Student.class);
        final List<Student> students = query.asList();

        System.out.println(students);

    }

    @Override
    public void dropDB(String dbName) {

    }

    @Override
    public void useDB(String dbName) {

    }

    @Override
    public void addStudents(List<Student> studentList) {

    }

    @Override
    public long removeStudents(List<String> studentIdsList) {
        return 0;
    }

    @Override
    public void updateStudents(List<Student> studentList) {

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
