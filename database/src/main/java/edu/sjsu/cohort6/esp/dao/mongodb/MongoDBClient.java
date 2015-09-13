package edu.sjsu.cohort6.esp.dao.mongodb;

import com.mongodb.MongoClient;
import edu.sjsu.cohort6.esp.dao.DBClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * A concrete DB Client implementation for MongoDB.
 *
 * @author rwatsh
 */
public class MongoDBClient implements DBClient {
    private Morphia morphia = null;
    private static MongoDBClient instance = null;
    private MongoClient mongoClient;
    private Datastore morphiaDatastore;

    public static void main(String[] args) {

        MongoDBClient client = new MongoDBClient("localhost", 27017);

        client.useDB("esp");
        final Student student = new Student("Watsh", "Rajneesh", "watsh.rajneesh@sjsu.edu", "password");
        client.morphiaDatastore.save(student);

        final Course course = new Course.Builder("Cloud Technologies")
                .maxCapacity(20)
                .price(200.0)
                .build();
        client.morphiaDatastore.save(course);

        student.getCourseRefs().add(course);
        client.morphiaDatastore.save(student);

        final Query<Student> query = client.morphiaDatastore.createQuery(Student.class);
        final List<Student> students = query.asList();

        System.out.println(students);

    }

    public MongoDBClient(String server, int port) {
        mongoClient = new MongoClient(server, port);
        morphia = new Morphia();
        morphia.map(Student.class);
        morphia.map(Course.class);
    }

    @Override
    public void dropDB(String dbName) {
        morphiaDatastore.getDB().dropDatabase();
    }

    @Override
    public void useDB(String dbName) {
        morphiaDatastore = morphia.createDatastore(mongoClient, dbName);
        morphiaDatastore.ensureIndexes();
    }

    @Override
    public void addStudents(List<Student> studentList) {
        morphiaDatastore.save(studentList);
    }

    @Override
    public long removeStudents(List<String> studentIdsList) {
        return morphiaDatastore.delete(Student.class, studentIdsList).getN();
    }

    @Override
    public void updateStudents(List<Student> studentList) {
        for (Student s : studentList) {
            UpdateOperations<Student> ops = morphiaDatastore.createUpdateOperations(Student.class)
                    .set("firstName", s.getFirstName())
                    .set("lastName", s.getLastName())
                    .set("emailId", s.getEmailId())
                    .set("passwordHash", s.getPasswordHash())
                    .set("courseRefs", s.getCourseRefs());

            Query<Student> updateQuery = morphiaDatastore.createQuery(Student.class).field(Mapper.ID_KEY).equal(s.getId());
            morphiaDatastore.update(updateQuery, ops);
        }
    }

    @Override
    public void updateStudents(List<Student> studentList, List<Course> courseList) {
        UpdateOperations<Student> ops = morphiaDatastore.createUpdateOperations(Student.class)
                .set("courseRefs", courseList);
        List<ObjectId> objectIds = new ArrayList<>();
        for (Student s : studentList) {
            objectIds.add(s.getId());
        }
        Query<Student> query = morphiaDatastore.createQuery(Student.class).field(Mapper.ID_KEY).in(objectIds);
        morphiaDatastore.update(query, ops);
    }

    @Override
    public List<Student> fetchStudents(List<String> studentIdsList) {
        List<ObjectId> objectIds = new ArrayList<>();
        for (String id : studentIdsList) {
            objectIds.add(new ObjectId(id));
        }
        return morphiaDatastore.find(Student.class).field(Mapper.ID_KEY).in(objectIds).asList();
    }

    @Override
    public void addCourse(List<Course> courseList) {
        morphiaDatastore.save(courseList);
    }

    @Override
    public long removeCourses(List<String> courseIdsList) {
        return morphiaDatastore.delete(Course.class, courseIdsList).getN();
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
        mongoClient.close();
    }
}
