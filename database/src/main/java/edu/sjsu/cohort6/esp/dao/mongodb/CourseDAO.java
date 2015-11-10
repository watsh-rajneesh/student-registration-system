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
import edu.sjsu.cohort6.esp.common.Course;
import edu.sjsu.cohort6.esp.dao.BaseDAO;
import edu.sjsu.cohort6.esp.dao.DBException;
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
 * Course DAO.
 *
 * Implements BaseDAO methods for the Couse entity.
 *
 * @author rwatsh
 */
public class CourseDAO extends BasicDAO<Course, String> implements BaseDAO<Course> {
    private Morphia morphia;
    protected CourseDAO(MongoClient mongoClient, Morphia morphia, String dbName) {
        super(mongoClient, morphia, dbName);
        this.morphia = morphia;
    }

    @Override
    public List<String> add(List<Course> entityList) throws DBException {
        try {
            //morphiaDatastore.save(courseList);
            List<String> insertedIds = new ArrayList<>();
            if (entityList != null) {
                for (Course course : entityList) {
                    Key<Course> key = this.save(course);
                    insertedIds.add(key.getId().toString());
                }
            }
            return insertedIds;
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public long remove(List<String> courseIdsList) {
        List<String> objectIds = new ArrayList<>();
        for (String id : courseIdsList) {
            objectIds.add(id);
        }
        Query<Course> query = this.createQuery().field(Mapper.ID_KEY).in(objectIds);
        return this.deleteByQuery(query).getN();
    }

    @Override
    public void update(List<Course> courseList) {
        for (Course course : courseList) {
            UpdateOperations<Course> ops = this.createUpdateOperations()
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

            Query<Course> updateQuery = this.createQuery().field(Mapper.ID_KEY).equal(course.getId());
            this.update(updateQuery, ops);
        }
    }

    @Override
    public List<Course> fetchById(List<String> courseIdsList) {
        List<String> objectIds = new ArrayList<>();
        if (courseIdsList != null) {
            for (String id : courseIdsList) {
                objectIds.add(id);
            }
        }

        if (!objectIds.isEmpty()) {
            Query<Course> query = this.createQuery().field(Mapper.ID_KEY).in(objectIds);
            QueryResults<Course> results = this.find(query);
            return results.asList();
        } else {
            Query<Course> query = this.createQuery();
            QueryResults<Course> results = this.find(query);
            return results.asList();
        }
    }

    @Override
    public List<Course> fetch(String query) {
        List<Course> courses = new ArrayList<>();
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
            Course course = morphia.fromDBObject(Course.class, dbObject);
            courses.add(course);
        }
        return courses;

    }

    public Course fetchCourseByName(String name) {
        List<Course> courses =  this.fetch("{courseName: \"" + name + "\"}");
        if (!courses.isEmpty()) {
            return courses.get(0);
        }
        return null;
    }
}
