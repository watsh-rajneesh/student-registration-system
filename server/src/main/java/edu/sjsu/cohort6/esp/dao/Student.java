package edu.sjsu.cohort6.esp.dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rwatsh on 9/7/15.
 */
public class Student implements DataModel {
    private String firstName;
    private String lastName;
    private String emailId;
    private List<String> courseRefs = new ArrayList<String>();

    public List<String> getCourseRefs() {
        return courseRefs;
    }

    public void setCourseRefs(List<String> courseRefs) {
        this.courseRefs = courseRefs;
    }

    public Student() {}

    public Student(String first, String last, String email) {
        this.firstName = first;
        this.lastName = last;
        this.emailId = email;
    }

    public void enrollCourses(List<String> courseRefs) {
        this.courseRefs = courseRefs;
    }

    public void unEnrollCourses(List<String> courseRefsToUnEnroll) {
        courseRefs.removeAll(courseRefsToUnEnroll);
    }

    public DBObject toDBObject() {
        BasicDBObjectBuilder documentBuilderDetail = BasicDBObjectBuilder.start()
                .add("firstName", this.firstName)
                .add("lastName", this.lastName)
                .add("emailId", this.emailId);

        for (String courseId: courseRefs) {
            documentBuilderDetail.add("courses", new BasicDBList()
                    .add(new BasicDBObject("$oid", courseId)));
        }

        return documentBuilderDetail.get();
    }

    public String getName() {
        return "student";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", courseRefs=" + courseRefs +
                '}';
    }
}
