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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.mongodb.morphia.annotations.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * A user of the system. User can have roles.
 * <p>
 * User documents are referenced by Student documents.
 * User documents will have embedded role info (whether user is admin or student).
 * <p>
 * "{
 * _id" : ObjectId("5603cf1ed3fde88bbc371ffd"),
 * "className" : "edu.sjsu.cohort6.esp.common.User",
 * "emailId" : "watsh.rajneesh@sjsu.edu",
 * "userName" : "watsh.rajneesh@sjsu.edu",
 * "token" : "3cc28509-350a-4e70-8f79-28798199f37b",
 * "firstName" : "Watsh",
 * "lastName" : "Rajneesh",
 * "role" : {
 * "role" : "STUDENT"
 * },
 * "lastUpdated" : ISODate("2015-09-24T10:23:26.095Z")
 * }
 *
 * @author rwatsh on 9/23/15.
 */
@Entity(value = "users" , noClassnameStored = true, concern = "SAFE")
public class User extends BaseModel {
    @Id
    private String id = new ObjectId().toHexString();

    @Indexed(unique = true)
    @NotNull
    @Email
    private String emailId;
    @NotNull
    private String userName;
    private String token;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @NotNull
    @Embedded
    private Role role;

    Date lastUpdated = new Date();

    @PrePersist
    void prePersist() {
        lastUpdated = new Date();
    }


    public User() {
    }

    public User(String emailId, String userName, String firstName, String lastName, Role role) {
        this.emailId = emailId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.token = randomUUID().toString();
        if (role == null) {
            this.role = new Role(RoleType.STUDENT);
        } else {
            this.role = role;
        }
    }

    @JsonProperty
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @JsonProperty
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id != null ? new ObjectId(id).toHexString() : new ObjectId().toHexString();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", emailId='" + emailId + '\'' +
                ", userName='" + userName + '\'' +
                ", token='" + token + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
