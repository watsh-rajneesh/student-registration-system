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

package edu.sjsu.cohort6.esp.dao.mongodb;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.mongodb.MongoClient;
import edu.sjsu.cohort6.esp.common.CommonUtils;
import edu.sjsu.cohort6.esp.common.Course;
import edu.sjsu.cohort6.esp.common.Student;
import edu.sjsu.cohort6.esp.dao.DBClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;

import java.text.MessageFormat;
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
    private MongoDBClient(@Assisted("server") String server, @Assisted("port") int port, @Assisted("dbName") String dbName) {
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
    }

    @Override
    public void dropDB(String dbName) {
        morphiaDatastore.getDB().dropDatabase();
    }

    @Override
    public void useDB(String dbName) {
        morphiaDatastore = morphia.createDatastore(mongoClient, dbName);
    }

    @Override
    public boolean checkHealth() {
        try {
            morphiaDatastore.getDB().getStats();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public String getConnectString() {
        return MessageFormat.format("DB Connect Info: server [{0}], port [{1}], dbName [{2}]", server, port, dbName);
    }

    @Override
    public List<String> addStudents(List<Student> studentList) {
        List<String> insertedIds = new ArrayList<>();
        for (Student s : studentList) {
            insertedIds.add(((ObjectId)studentDAO.save(s).getId()).toString());
        }
        return insertedIds;
    }

    @Override
    public long removeStudents(List<String> studentIdsList) {
        List<ObjectId> objectIds = new ArrayList<>();
        for (String id : studentIdsList) {
            objectIds.add(new ObjectId(id));
        }
        Query<Student> query = studentDAO.createQuery().field(Mapper.ID_KEY).in(objectIds);
        return studentDAO.deleteByQuery(query).getN();
    }

    @Override
    public void updateStudents(List<Student> studentList) {
        for (Student s : studentList) {
            UpdateOperations<Student> ops = studentDAO.createUpdateOperations()
                    .set("firstName", s.getFirstName())
                    .set("lastName", s.getLastName())
                    .set("emailId", s.getEmailId())
                    .set("passwordHash", s.getPasswordHash())
                    .set("courseRefs", s.getCourseRefs());

            Query<Student> updateQuery = morphiaDatastore.createQuery(Student.class).field(Mapper.ID_KEY).equal(s.get_id());
            studentDAO.update(updateQuery, ops);
        }
    }

    @Override
    public void updateStudents(List<Student> studentList, List<Course> courseList) {
        UpdateOperations<Student> ops = studentDAO.createUpdateOperations()
                .set("courseRefs", courseList);
        List<ObjectId> objectIds = new ArrayList<>();
        for (Student s : studentList) {
            objectIds.add(s.get_id());
        }
        Query<Student> query = studentDAO.createQuery().field(Mapper.ID_KEY).in(objectIds);
        studentDAO.update(query, ops);
    }

    @Override
    public List<Student> fetchStudents(List<String> studentIdsList) {
        List<ObjectId> objectIds = new ArrayList<>();
        Query<Student> query =  null;

        if (studentIdsList != null) {
            for (String id : studentIdsList) {
                if (id != null) {
                    id = CommonUtils.sanitizeIdString(id);
                    objectIds.add(new ObjectId(id));
                }
            }
        }
        query = objectIds != null && !objectIds.isEmpty()
                ? studentDAO.createQuery().field(Mapper.ID_KEY).in(objectIds)
                : studentDAO.createQuery();
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
                    //.set("endDate", course.getEndDate())
                    .set("endTime", course.getEndTime())
                    .set("instructors", course.getInstructors())
                    .set("keywords", course.getKeywords())
                    .set("location", course.getLocation())
                    .set("maxCapacity", course.getMaxCapacity())
                    .set("price", course.getPrice())
                    //.set("startDate", course.getStartDate())
                    .set("startTime", course.getStartTime());

            Query<Course> updateQuery = courseDAO.createQuery().field(Mapper.ID_KEY).equal(course.get_id());
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
