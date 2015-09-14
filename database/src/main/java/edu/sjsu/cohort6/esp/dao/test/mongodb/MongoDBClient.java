package edu.sjsu.cohort6.esp.dao.test.mongodb;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.mongodb.MongoClient;
import edu.sjsu.cohort6.esp.dao.test.DBClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * A concrete DB Client implementation for MongoDB.
 *
 * This DB client implementation will use the DAO instances to operate on the DB.
 *
 * @author rwatsh
 */
public class MongoDBClient implements DBClient {
    private final String server;
    private final int port;
    private final String dbName;
    private CourseDAO courseDAO;
    private Morphia morphia = null;
    private static MongoDBClient instance = null;
    private MongoClient mongoClient;
    private Datastore morphiaDatastore;
    private StudentDAO studentDAO;

    public static void main(String[] args) {

        MongoDBClient client = new MongoDBClient("localhost", 27017, "esp");

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

    /**
     * Constructs a MongoDB client instance.
     *
     * This is private so it can only be instantiated via DI (using Guice).
     *
     * @param server    server hostname or ip
     * @param port      port number for mongodb service
     * @param dbName    name of db to use
     */
    @Inject
    private MongoDBClient(@Assisted("server") String server, @Assisted("port") int port, @Assisted String dbName) {
        this.server = server;
        this.port = port;
        this.dbName = dbName;
        mongoClient = new MongoClient(server, port);
        morphia = new Morphia();
        morphia.map(Student.class);
        morphia.map(Course.class);
        studentDAO = new StudentDAO(mongoClient, morphia, dbName);
        courseDAO = new CourseDAO(mongoClient, morphia, dbName);
        morphiaDatastore = morphia.createDatastore(mongoClient, dbName);
        //morphiaDatastore.ensureIndexes();
    }

    @Override
    public void dropDB(String dbName) {
        morphiaDatastore.getDB().dropDatabase();
    }

    @Override
    public void useDB(String dbName) {
        morphiaDatastore = morphia.createDatastore(mongoClient, dbName);
        //morphiaDatastore.ensureIndexes();
    }

    @Override
    public List<String> addStudents(List<Student> studentList) {
        //morphiaDatastore.save(studentList);
        List<String> insertedIds = new ArrayList<>();
        for (Student s : studentList) {
            insertedIds.add(((ObjectId)studentDAO.save(s).getId()).toString());
        }
        return insertedIds;
    }

    @Override
    public long removeStudents(List<String> studentIdsList) {
        //return morphiaDatastore.delete(Student.class, studentIdsList).getN();
        List<ObjectId> objectIds = new ArrayList<>();
        for (String id : studentIdsList) {
            objectIds.add(new ObjectId(id));
        }
        Query<Student> query = studentDAO.createQuery().field(Mapper.ID_KEY).in(objectIds);
        return studentDAO.deleteByQuery(query).getN();
    }

    @Override
    public void updateStudents(List<Student> studentList) {
        /*for (Student s : studentList) {
            UpdateOperations<Student> ops = morphiaDatastore.createUpdateOperations(Student.class)
                    .set("firstName", s.getFirstName())
                    .set("lastName", s.getLastName())
                    .set("emailId", s.getEmailId())
                    .set("passwordHash", s.getPasswordHash())
                    .set("courseRefs", s.getCourseRefs());

            Query<Student> updateQuery = morphiaDatastore.createQuery(Student.class).field(Mapper.ID_KEY).equal(s.getId());
            morphiaDatastore.update(updateQuery, ops);
        }*/
        for (Student s : studentList) {
            UpdateOperations<Student> ops = studentDAO.createUpdateOperations()
                    .set("firstName", s.getFirstName())
                    .set("lastName", s.getLastName())
                    .set("emailId", s.getEmailId())
                    .set("passwordHash", s.getPasswordHash())
                    .set("courseRefs", s.getCourseRefs());

            Query<Student> updateQuery = morphiaDatastore.createQuery(Student.class).field(Mapper.ID_KEY).equal(s.getId());
            studentDAO.update(updateQuery, ops);
        }
    }

    @Override
    public void updateStudents(List<Student> studentList, List<Course> courseList) {
        /*UpdateOperations<Student> ops = morphiaDatastore.createUpdateOperations(Student.class)
                .set("courseRefs", courseList);
        List<ObjectId> objectIds = new ArrayList<>();
        for (Student s : studentList) {
            objectIds.add(s.getId());
        }
        Query<Student> query = morphiaDatastore.createQuery(Student.class).field(Mapper.ID_KEY).in(objectIds);
        morphiaDatastore.update(query, ops);*/
        UpdateOperations<Student> ops = studentDAO.createUpdateOperations()
                .set("courseRefs", courseList);
        List<ObjectId> objectIds = new ArrayList<>();
        for (Student s : studentList) {
            objectIds.add(s.getId());
        }
        Query<Student> query = studentDAO.createQuery().field(Mapper.ID_KEY).in(objectIds);
        studentDAO.update(query, ops);
    }

    @Override
    public List<Student> fetchStudents(List<String> studentIdsList) {
        List<ObjectId> objectIds = new ArrayList<>();
        for (String id : studentIdsList) {
            objectIds.add(new ObjectId(id));
        }
        /*return morphiaDatastore.find(Student.class).field(Mapper.ID_KEY).in(objectIds).asList();*/

        Query<Student> query = studentDAO.createQuery().field(Mapper.ID_KEY).in(objectIds);
        QueryResults<Student> results = studentDAO.find(query);
        return results.asList();
    }

    @Override
    public List<String> addCourse(List<Course> courseList) {
        //morphiaDatastore.save(courseList);
        List<String> insertedIds = new ArrayList<>();
        for (Course course: courseList) {
            insertedIds.add(((ObjectId) courseDAO.save(course).getId()).toString());
        }
        return insertedIds;
    }

    @Override
    public long removeCourses(List<String> courseIdsList) {
        //return morphiaDatastore.delete(Course.class, courseIdsList).getN();
        List<ObjectId> objectIds = new ArrayList<>();
        for (String id : courseIdsList) {
            objectIds.add(new ObjectId(id));
        }
        Query<Course> query = courseDAO.createQuery().field(Mapper.ID_KEY).in(objectIds);
        return courseDAO.deleteByQuery(query).getN();
    }

    @Override
    public void updateCourses(List<Course> courseList) {
        for (Course course : courseList) {
            UpdateOperations<Course> ops = courseDAO.createUpdateOperations()
                    .set("courseName", course.getCourseName())
                    .set("availabilityStatus", course.getAvailabilityStatus())
                    .set("endDate", course.getEndDate())
                    .set("endTime", course.getEndTime())
                    .set("instructors", course.getInstructors())
                    .set("keywords", course.getKeywords())
                    .set("location", course.getLocation())
                    .set("maxCapacity", course.getMaxCapacity())
                    .set("price", course.getPrice())
                    .set("startDate", course.getStartDate())
                    .set("startTime", course.getStartTime());

            Query<Course> updateQuery = courseDAO.createQuery().field(Mapper.ID_KEY).equal(course.getId());
            courseDAO.update(updateQuery, ops);
        }
    }

    @Override
    public List<Course> fetchCourses(List<String> courseIdsList) {
        List<ObjectId> objectIds = new ArrayList<>();
        for (String id : courseIdsList) {
            objectIds.add(new ObjectId(id));
        }

        Query<Course> query = courseDAO.createQuery().field(Mapper.ID_KEY).in(objectIds);
        QueryResults<Course> results = courseDAO.find(query);
        return results.asList();
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }
}
