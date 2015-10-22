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

import com.google.common.base.Optional;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.dao.BaseDAO;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * User DAO.
 *
 * Implements BaseDAO methods for the User entity.
 *
 * @author rwatsh on 9/24/15.
 */
public class UserDAO extends BasicDAO<User, String> implements BaseDAO<User> {

    private Morphia morphia;

    protected UserDAO(MongoClient mongoClient, Morphia morphia, String dbName) {
        super(mongoClient, morphia, dbName);
        this.morphia = morphia;
    }

    @Override
    public List<String> add(List<User> entityList) {
        List<String> insertedIds = new ArrayList<>();

        if (entityList != null) {
            for (User user: entityList) {
                Key<User> key = this.save(user);
                insertedIds.add(key.getId().toString());
            }
        }
        return insertedIds;
    }

    @Override
    public long remove(List<String> entityIdsList) {
        List<String> objectIds = new ArrayList<>();
        for (String id : entityIdsList) {
            objectIds.add(id);
        }
        Query<User> query = this.createQuery().field(Mapper.ID_KEY).in(objectIds);
        return this.deleteByQuery(query).getN();
    }

    @Override
    public void update(List<User> entityList) {
        for (User u : entityList) {
            UpdateOperations<User> ops = this.createUpdateOperations()
                    .set("emailId", u.getEmailId())
                    .set("lastName", u.getLastName())
                    .set("firstName", u.getFirstName())
                    .set("userName", u.getUserName())
                    .set("token", u.getToken());

            Query<User> updateQuery = this.createQuery().field(Mapper.ID_KEY).equal(u.getId());
            this.update(updateQuery, ops);
        }
    }

    @Override
    public List<User> fetchById(List<String> entityIdsList) {
        List<String> objectIds = new ArrayList<>();
        Query<User> query =  null;

        if (entityIdsList != null) {
            for (String id : entityIdsList) {
                if (id != null) {
                    //id = CommonUtils.sanitizeIdString(id);
                    objectIds.add(id);
                }
            }
        }
        query = objectIds != null && !objectIds.isEmpty()
                ? this.createQuery().field(Mapper.ID_KEY).in(objectIds)
                : this.createQuery();
        QueryResults<User> results = this.find(query);
        return results.asList();
    }

    @Override
    public List<User> fetch(String query) {
        List<User> users = new ArrayList<>();
        DBObject dbObjQuery;
        DBCursor cursor;
        if (!(query == null)) {
            dbObjQuery = (DBObject) JSON.parse(query);
            cursor = this.getCollection().find(dbObjQuery);
        } else {
            cursor = this.getCollection().find();
        }

        List<DBObject> dbObjects = cursor.toArray();
        for (DBObject dbObject: dbObjects) {
            User user = morphia.fromDBObject(User.class, dbObject);
            users.add(user);
        }
        return users;
    }

    /**
     * This method is required for basic authentication upon every API call.
     *
     * @param username
     * @param password
     * @return
     */
    public Optional<User> getUserByCredentials(String username, String password) throws RuntimeException {
        Query<User> query =  this.createQuery().field("userName").equal(username).field("token").equal(password);
        QueryResults<User> results = this.find(query);
        if (results != null) {
            return Optional.fromNullable(results.get());
        } else {
            throw new RuntimeException("No user exists, please create an admin user account");
        }
    }
}
