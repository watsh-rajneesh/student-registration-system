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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Student entity.
 *
 * Some annotations are per morphia and some per dropwizard frameworks.
 * This POJO will be used across DB, Web service and UI.
 *
 * {
     "_id" : ObjectId("55f6f9fcd3fde80ee829ef4c"),
     "className" : "edu.sjsu.cohort6.esp.common.Student",
     "firstName" : "Watsh",
     "lastName" : "test",
     "emailId" : "watsh.rajneesh@sjsu.edu",
     "passwordHash" : "5f4dcc3b5aa765d61d8327deb882cf99",
     "lastUpdated" : ISODate("2015-09-14T16:46:52.352Z"),
     "courseRefs" : [ ]
 }
 *
 * @author rwatsh
 */
@Entity("student")
@Indexes(
        @Index(value = "emailId", fields = @Field("emailId"))
)
@JsonIgnoreProperties({"_id", "code", "message"})
public class Student {
    @Id
    private ObjectId _id;
    @Transient
    private String id;
    private String firstName;
    private String lastName;
    @Indexed(name="emailId", unique=true,dropDups=true)
    private String emailId;
    private String passwordHash;
    @Reference
    private List<Course> courseRefs;

    Date lastUpdated = new Date();

    @PrePersist void prePersist() {lastUpdated = new Date();}

    public Student(String firstName, String lastName, String emailId, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        try {
            this.passwordHash = generateMD5Hash(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate password hash");
        }
        this.courseRefs = new ArrayList<>();
    }

    public Student() {}

    private String generateMD5Hash(String plaintext) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    @JsonProperty
    public String getId() {
        return _id != null ? _id.toHexString() : null;
    }

    public void setId(String id) {
        this.id = id;
        this._id = new ObjectId(id);
    }

    @JsonProperty
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @JsonProperty
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @JsonProperty
    public List<Course> getCourseRefs() {
        return courseRefs;
    }

    public void setCourseRefs(List<Course> courseRefs) {
        this.courseRefs = courseRefs;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + (_id != null ?_id.toHexString() : _id) +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", courseRefs=" + courseRefs +
                '}';
    }
}
