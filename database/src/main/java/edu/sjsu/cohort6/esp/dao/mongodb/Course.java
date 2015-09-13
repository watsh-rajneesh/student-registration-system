package edu.sjsu.cohort6.esp.dao.mongodb;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * Created by rwatsh on 9/7/15.
 */
@Entity("course")
@Indexes(
        @Index(value = "courseName", fields = @Field("courseName"))
)
public class Course {
    @Id
    private ObjectId id;
    @Indexed(name="courseName", unique=true,dropDups=true)
    private String courseName;
    private List<String> instructors;
    private Date startTime;
    private Date endTime;
    private Date startDate;
    private Date endDate;
    private Integer availabilityStatus;
    private Integer maxCapacity;
    private Double price;
    private String location;
    private List<String> keywords;
    /*@Reference
    private List<Student> studentRefs;*/

    Date lastUpdated = new Date();

    @PrePersist void prePersist() {lastUpdated = new Date();}

    public Course() {}

    public static class Builder {
        private String courseName;
        private List<String> instructors;
        private Date startTime;
        private Date endTime;
        private Date startDate;
        private Date endDate;
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

        public Builder startDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(Date endDate) {
            this.endDate = endDate;
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
        this.endDate = b.endDate;
        this.endTime = b.endTime;
        this.instructors = b.instructors;
        this.keywords = b.keywords;
        this.location = b.location;
        this.maxCapacity = b.maxCapacity;
        this.price = b.price;
        this.startDate = b.startDate;
        this.startTime = b.startTime;
        //this.studentRefs = b.studentRefs;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<String> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<String> instructors) {
        this.instructors = instructors;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(Integer availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

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
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", instructors=" + instructors +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", availabilityStatus=" + availabilityStatus +
                ", maxCapacity=" + maxCapacity +
                ", price=" + price +
                ", location='" + location + '\'' +
                ", keywords=" + keywords +
                //", studentRefs=" + studentRefs +
                '}';
    }
}
