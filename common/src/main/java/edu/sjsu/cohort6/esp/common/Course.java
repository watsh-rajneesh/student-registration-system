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
import org.mongodb.morphia.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Course entity.
 * db.course.find().pretty()
 * {
 * "_id" : ObjectId("5603cf1ed3fde88bbc371fff"),
 * "className" : "edu.sjsu.cohort6.esp.common.Course",
 * "courseName" : "Cloud Technologies",
 * "instructors" : [
 * "Ahmad Nouri",
 * "Thomas Hildebrand",
 * "Aktouf"
 * ],
 * "startTime" : ISODate("2015-10-10T17:30:00Z"),
 * "endTime" : ISODate("2015-11-10T21:00:00Z"),
 * "availabilityStatus" : 1,
 * "maxCapacity" : 20,
 * "price" : 200,
 * "location" : "Santa Clara, CA",
 * "keywords" : [
 * "Java",
 * "MongoDB",
 * "REST"
 * ],
 * "lastUpdated" : ISODate("2015-09-24T10:23:26.102Z")
 * }
 *
 * @author rwatsh
 */
@Entity("course")
@Indexes(
        @Index(value = "courseName", fields = @Field("courseName"))
)
public class Course extends BaseModel {
    @Id
    private ObjectId id;

    @Indexed(name = "courseName", unique = true, dropDups = true)
    private String courseName;
    private List<String> instructors;
    private Date startTime;
    private Date endTime;
    private Integer availabilityStatus;
    private Integer maxCapacity;
    private Double price;
    private String location;
    private List<String> keywords;

    /*@Reference
    private List<Student> studentRefs;*/

    Date lastUpdated = new Date();

    private static final Logger log = Logger.getLogger(Course.class.getName());

    @PrePersist
    void prePersist() {
        lastUpdated = new Date();
    }

    public Course() {
    }

    public static class Builder {
        private String courseName;
        private List<String> instructors;
        private Date startTime;
        private Date endTime;
        private Integer availabilityStatus = 1;
        private Integer maxCapacity;
        private Double price = 0.0;
        private String location;
        private List<String> keywords;
        //private List<Student> studentRefs;

        public Builder(String courseName) {
            this.courseName = courseName;
            //this.studentRefs = new ArrayList<>();
        }

        public Builder instructors(List<String> instructors) {
            this.instructors = instructors;
            return this;
        }

        public Builder startTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder availabilityStatus(Integer availabilityStatus) {
            this.availabilityStatus = availabilityStatus;
            return this;
        }

        public Builder maxCapacity(Integer maxCapacity) {
            this.maxCapacity = maxCapacity;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder keywords(List<String> keywords) {
            this.keywords = keywords;
            return this;
        }

        public Course build() {
            return new Course(this);
        }
    }

    private Course(Builder b) {
        this.availabilityStatus = b.availabilityStatus;
        this.courseName = b.courseName;
        this.endTime = b.endTime;
        this.instructors = b.instructors;
        this.keywords = b.keywords;
        this.location = b.location;
        this.maxCapacity = b.maxCapacity;
        this.price = b.price;
        this.startTime = b.startTime;
        //this.studentRefs = b.studentRefs;
    }

    @JsonProperty
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }


    @JsonProperty
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @JsonProperty
    public List<String> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<String> instructors) {
        this.instructors = instructors;
    }

    @JsonProperty
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonProperty
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @JsonProperty
    public Integer getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(Integer availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    @JsonProperty
    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @JsonProperty
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonProperty
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty
    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    /*public List<Student> getStudentRefs() {
        return studentRefs;
    }

    public void setStudentRefs(List<Student> studentRefs) {
        this.studentRefs = studentRefs;
    }*/

    @Override
    public String toString() {
        return "Course{" +
                "id=" + (id != null ? id.toHexString() : "") +
                ", courseName='" + courseName + '\'' +
                ", instructors=" + instructors +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", availabilityStatus=" + availabilityStatus +
                ", maxCapacity=" + maxCapacity +
                ", price=" + price +
                ", location='" + location + '\'' +
                ", keywords=" + keywords +
                //", studentRefs=" + studentRefs +
                '}';
    }

    public static enum AvailabilityStatus {
        AVAILABLE(1), UNAVAILABLE(0);
        private int value;

        private AvailabilityStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


}
