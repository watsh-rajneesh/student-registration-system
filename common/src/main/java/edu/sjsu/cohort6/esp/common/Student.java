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

package edu.sjsu.cohort6.esp.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Student entity.
 * <p>
 * Student document will have a user entity reference.
 * Student document may have one or more course references.
 * <p>
 * Some annotations are per morphia and some per dropwizard frameworks.
 * This POJO will be used across DB, Web service and UI.
 * <p>
 * {
 * "_id" : ObjectId("5603cf1ed3fde88bbc372003"),
 * "className" : "edu.sjsu.cohort6.esp.common.Student",
 * "user" : DBRef("user", ObjectId("5603cf1ed3fde88bbc372002")),
 * "lastUpdated" : ISODate("2015-09-24T10:23:26.128Z")
 * }
 *
 * @author rwatsh
 */
@Entity(value = "course" , noClassnameStored = true, concern = "SAFE")
@JsonIgnoreProperties({"_id"})
public class Student extends BaseModel {
    @Id
    private ObjectId _id;

    @Transient
    private String id;

    @Reference
    private User user;

    @Reference
    private List<Course> courseRefs;

    Date lastUpdated = new Date();

    @PrePersist
    void prePersist() {
        lastUpdated = new Date();
    }

    private static final Logger log = Logger.getLogger(Student.class.getName());

    public Student(User user) {
        this.user = user;
        this.courseRefs = new ArrayList<>();
    }

    public Student() {
    }


    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId id) {
        this._id = id;
    }

    @JsonProperty
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    @JsonProperty
    public List<Course> getCourseRefs() {
        return courseRefs;
    }

    public void setCourseRefs(List<Course> courseRefs) {
        this.courseRefs = courseRefs;
    }


    @JsonProperty
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonProperty
    public String getId() {
        return _id != null? _id.toHexString() : null;
    }

    public void setId(String id) {
        this._id = new ObjectId(id);
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + (_id != null ? _id.toHexString() : "") +
                ", user=" + user +
                ", courseRefs=" + courseRefs +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
