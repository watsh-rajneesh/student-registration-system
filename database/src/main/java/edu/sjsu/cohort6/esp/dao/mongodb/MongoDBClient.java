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
import edu.sjsu.cohort6.esp.common.*;
import edu.sjsu.cohort6.esp.dao.BaseDAO;
import edu.sjsu.cohort6.esp.dao.DBClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.text.MessageFormat;
import java.util.List;

/**
 * A concrete DB Client implementation for MongoDB.
 *
 * This DB client implementation will use the DAO instances to operate on the DB.
 *
 * It will initialize the DB client interface and the respective DAOs. It provides a method for user to get
 * respective DAO instances so they can perform model specific DB operations.
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
    private UserDAO userDAO;

    public static void main(String[] args) {

        MongoDBClient client = new MongoDBClient("localhost", 27017, "esp");

        client.useDB("esp");
        final User user = new User("watsh.rajneesh@sjsu.edu", "Watsh", "Rajneesh", "watsh.rajneesh@sjsu.edu", new Role(RoleType.ADMIN));
        client.userDAO.save(user);
        final Student student = new Student(user);
        client.studentDAO.save(student);

        final Course course = new Course.Builder("Cloud Technologies")
                .maxCapacity(20)
                .price(200.0)
                .build();
        client.courseDAO.save(course);

        student.getCourseRefs().add(course);
        client.studentDAO.save(student);

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
        morphia.mapPackageFromClass(Student.class);
        morphiaDatastore = morphia.createDatastore(mongoClient, dbName);
        studentDAO = new StudentDAO(mongoClient, morphia, dbName);
        courseDAO = new CourseDAO(mongoClient, morphia, dbName);
        userDAO = new UserDAO(mongoClient, morphia, dbName);
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
            mongoClient.listDatabaseNames();
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
    public Object getDAO(Class<? extends BaseDAO> clazz) {
        if (clazz != null) {
            if (clazz.getTypeName().equalsIgnoreCase(StudentDAO.class.getTypeName())) {
                return studentDAO;
            } else if (clazz.getTypeName().equalsIgnoreCase(CourseDAO.class.getTypeName())) {
                return courseDAO;
            } else if (clazz.getTypeName().equalsIgnoreCase(UserDAO.class.getTypeName())) {
                return userDAO;
            }
        }
        return null;
    }

    @Override
    public Morphia getMorphia() {
        return morphia;
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }
}
