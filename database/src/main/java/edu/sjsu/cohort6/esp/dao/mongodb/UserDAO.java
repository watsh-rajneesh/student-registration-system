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
import com.mongodb.MongoClient;
import edu.sjsu.cohort6.esp.common.CommonUtils;
import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.dao.BaseDAO;
import org.bson.types.ObjectId;
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

    protected UserDAO(MongoClient mongoClient, Morphia morphia, String dbName) {
        super(mongoClient, morphia, dbName);
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
        List<ObjectId> objectIds = new ArrayList<>();
        for (String id : entityIdsList) {
            objectIds.add(new ObjectId(id));
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
                    .set("userName", u.getUserName());

            Query<User> updateQuery = this.createQuery().field(Mapper.ID_KEY).equal(u.get_id());
            this.update(updateQuery, ops);
        }
    }

    @Override
    public List<User> fetch(List<String> entityIdsList) {
        List<ObjectId> objectIds = new ArrayList<>();
        Query<User> query =  null;

        if (entityIdsList != null) {
            for (String id : entityIdsList) {
                if (id != null) {
                    id = CommonUtils.sanitizeIdString(id);
                    objectIds.add(new ObjectId(id));
                }
            }
        }
        query = objectIds != null && !objectIds.isEmpty()
                ? this.createQuery().field(Mapper.ID_KEY).in(objectIds)
                : this.createQuery();
        QueryResults<User> results = this.find(query);
        return results.asList();
    }

    /**
     * This method is required for basic authentication upon every API call.
     *
     * @param username
     * @param password
     * @return
     */
    public Optional<User> getUserByCredentials(String username, String password) {
        Query<User> query =  this.createQuery().field("userName").equal(username).field("token").equal(password);
        QueryResults<User> results = this.find(query);
        return Optional.of(results.get());
    }
}
