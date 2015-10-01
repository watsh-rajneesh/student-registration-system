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

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import edu.sjsu.cohort6.esp.common.Student;
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
 * Student DAO.
 *
 * Implements BaseDAO methods for the student entity.
 *
 * @author rwatsh
 */
public class StudentDAO extends BasicDAO<Student, String> implements BaseDAO<Student> {
    private Morphia morphia;

    protected StudentDAO(MongoClient mongoClient, Morphia morphia, String dbName) {
        super(mongoClient, morphia, dbName);
        this.morphia = morphia;
    }

    /**
     * Add a list of students to DB.
     *
     * @param entityList
     * @return
     */
    @Override
    public List<String> add(List<Student> entityList) {
        List<String> insertedIds = new ArrayList<>();

        if (entityList != null) {
            for (Student student: entityList) {
                Key<Student> key = this.save(student);
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
        Query<Student> query = this.createQuery().field(Mapper.ID_KEY).in(objectIds);
        return this.deleteByQuery(query).getN();
    }

    @Override
    public void update(List<Student> studentList) {
        for (Student s : studentList) {
            UpdateOperations<Student> ops = this.createUpdateOperations()
                    .set("courseRefs", s.getCourseRefs())
                    .set("user", s.getUser());

            Query<Student> updateQuery = this.createQuery().field(Mapper.ID_KEY).equal(s.get_id());
            this.update(updateQuery, ops);
        }
    }

    @Override
    public List<Student> fetchById(List<String> studentIdsList) {
        List<ObjectId> objectIds = new ArrayList<>();
        Query<Student> query =  null;

        if (studentIdsList != null) {
            for (String id : studentIdsList) {
                if (id != null) {
                    //id = CommonUtils.sanitizeIdString(id);
                    objectIds.add(new ObjectId(id));
                }
            }
        }
        query = objectIds != null && !objectIds.isEmpty()
                ? this.createQuery().field(Mapper.ID_KEY).in(objectIds)
                : this.createQuery();
        QueryResults<Student> results = this.find(query);
        return results.asList();
    }

    @Override
    public List<Student> fetch(String query) {
        List<Student> students = new ArrayList<>();
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
            Student student = morphia.fromDBObject(Student.class, dbObject);
            students.add(student);
        }
        return students;
    }



    /*@Override
    public void updateStudents(List<Student> studentList, List<Course> courseList) {
        UpdateOperations<Student> ops = studentDAO.createUpdateOperations()
                .set("courseRefs", courseList);
        List<ObjectId> objectIds = new ArrayList<>();
        for (Student s : studentList) {
            objectIds.add(s.get_id());
        }
        Query<Student> query = studentDAO.createQuery().field(Mapper.ID_KEY).in(objectIds);
        studentDAO.update(query, ops);
    }*/

}
