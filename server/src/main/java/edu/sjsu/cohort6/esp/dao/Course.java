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
public class Course implements DataModel{
    private String courseName;
    private List<String> studentRefs = new ArrayList<String>();

    public List<String> getStudentRefs() {
        return studentRefs;
    }

    public void setStudentRefs(List<String> studentRefs) {
        this.studentRefs = studentRefs;
    }

    public Course() {}

    public Course(String name) {
        this.courseName = name;
    }

    public void enrollStudents(List<String> studentRefs) {
        this.studentRefs = studentRefs;
    }

    public void unEnrollStudents(List<String> studentRefsToUnEnroll) {
        this.studentRefs.removeAll(studentRefsToUnEnroll);
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }


    public DBObject toDBObject() {
        BasicDBObjectBuilder documentBuilderDetail = BasicDBObjectBuilder.start()
                .add("courseName", this.courseName);


        for (String studentId: studentRefs) {
            documentBuilderDetail.add("courses", new BasicDBList()
                    .add(new BasicDBObject("$oid", studentId)));
        }

        return documentBuilderDetail.get();
    }

    public String getName() {
        return "course";
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseName='" + courseName + '\'' +
                ", studentRefs=" + studentRefs +
                '}';
    }
}
