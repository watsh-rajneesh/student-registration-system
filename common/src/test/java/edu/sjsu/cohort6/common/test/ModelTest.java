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

package edu.sjsu.cohort6.common.test;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import edu.sjsu.cohort6.esp.common.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author rwatsh on 9/29/15.
 */
public class ModelTest {
    private static final Logger log = Logger.getLogger(ModelTest.class.getName());
    private Morphia morphia;
    private MongoClient mongoClient;
    private Datastore morphiaDatastore;

    @BeforeClass
    public void setUp() {
        mongoClient = new MongoClient("localhost", 27017);
        morphia = new Morphia();
        morphia.mapPackageFromClass(Student.class);
        morphiaDatastore = morphia.createDatastore(mongoClient, "testmodel");
    }


    @Test
    public void testAdd() throws IOException {
        User user = new User("test@gmail.com", "test@gmail.com", "John", "Doe", new Role(RoleType.ADMIN) );
        String jsonStr = CommonUtils.convertObjectToJson(user);
        log.info(jsonStr);
        DBObject dbObject = (DBObject) JSON.parse(jsonStr);
        Key<User> userKey = morphiaDatastore.save(user);
        User savedUser = morphiaDatastore.getByKey(User.class, userKey);
        log.info(dbObject.toString());
        log.info(CommonUtils.convertObjectToJson(savedUser));


        Student student = new Student(user);
        jsonStr = CommonUtils.convertObjectToJson(student);
        log.info(jsonStr);

        dbObject = (DBObject) JSON.parse(jsonStr);
    }
}
