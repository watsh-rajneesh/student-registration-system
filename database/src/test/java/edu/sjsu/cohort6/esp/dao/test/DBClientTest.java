package edu.sjsu.cohort6.esp.dao.test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import edu.sjsu.cohort6.esp.dao.test.mongodb.Course;
import edu.sjsu.cohort6.esp.dao.test.mongodb.Student;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Unit test for DB Client.
 *
 * @author rwatsh
 */
public class DBClientTest {

    @Inject
    DBFactory dbFactory;

    public static final String TESTREG = "testreg";
    private static final Logger log = Logger.getLogger(DBClientTest.class.getName());
    private DBClient client;

    @BeforeTest
    public void setUp() throws Exception {
        Module module = new DatabaseModule();
        Guice.createInjector(module).injectMembers(this);
        client = dbFactory.create("localhost", 27017, TESTREG);
        client.dropDB(TESTREG);
    }


    @AfterTest
    public void tearDown() throws Exception {
        client.close();
    }

    @BeforeMethod
    public void createDB() {
        client.useDB(TESTREG);
    }

    @AfterMethod
    public void dropDB() {
        //client.dropDB(TESTREG);
    }



    @Test
    public void testAddStudents() {
        testCreateStudents();
    }

    private List<String> testCreateStudents() {
        Student s = new Student("Watsh", "Rajneesh", "watsh.rajneesh@sjsu.edu", "password");
        List<Student> studentList = new ArrayList<>();
        studentList.add(s);
        List<String> insertedIds = client.addStudents(studentList);
        List<Student> students = client.fetchStudents(insertedIds);
        Assert.assertNotNull(students);
        log.info("Student created: " + students);
        return insertedIds;
    }

    @Test
    public void testRemoveStudents() throws Exception {
        List<String> insertedIds = testCreateStudents();
        Assert.assertNotNull(insertedIds);
        long countRemovedEntries = client.removeStudents(insertedIds);
        Assert.assertTrue(countRemovedEntries > 0, "Failed to delete any student");
    }

    @Test
    public void testUpdateStudents() throws Exception {
        List<String> insertedIds = testCreateStudents();
        Assert.assertNotNull(insertedIds);
        List<Student> students = client.fetchStudents(insertedIds);
        Assert.assertNotNull(students);
        for (Student s : students) {
            s.setLastName("test");
        }
        log.info("Student modified: " + students);
        client.updateStudents(students);
        students = client.fetchStudents(insertedIds);
        Assert.assertNotNull(students);
        log.info("Student updated in DB: " + students);
    }

    @Test
    public void testEnrollStudents() throws Exception {
        List<String> insertedIds = testCreateStudents();
        Assert.assertNotNull(insertedIds);
        List<Student> students = client.fetchStudents(insertedIds);
        Assert.assertNotNull(students);
        for (Student s : students) {
            s.setLastName("test");
        }
        log.info("Student modified: " + students);
        client.updateStudents(students);
        students = client.fetchStudents(insertedIds);
        Assert.assertNotNull(students);
        log.info("Student updated in DB: " + students);
    }

    @Test
    public void testFetchStudents() throws Exception {
        List<String> insertedIds = testCreateStudents();
        Assert.assertNotNull(insertedIds);
        List<Student> students = client.fetchStudents(insertedIds);
        Assert.assertNotNull(students);
    }

    @Test
    public void testAddCourse() throws ParseException {
        testCreateCourse();
    }

    private List<String> testCreateCourse() throws ParseException {
        ArrayList<String> keywords = new ArrayList<String>() {{
            add("Java");
            add("MongoDB");
            add("REST");
        }};
        ArrayList<String> instructors = new ArrayList<String>() {{
            add("Ahmad Nouri");
            add("Thomas Hildebrand");
            add("Aktouf");
        }};
        Course course = new Course.Builder("Cloud Technologies")
                .maxCapacity(20)
                .price(200.0)
                .availabilityStatus(Course.AvailabilityStatus.AVAILABLE.getValue())
                .startDate(getDateFromString("10-10-2015"))
                .endDate(getDateFromString("11-10-2015"))
                .startTime(getTimeFromString("10:30"))
                .endTime(getTimeFromString("13:00"))
                .instructors(instructors)
                .location("Santa Clara, CA")
                .keywords(keywords)
                .build();
        log.info("Course : " + course);
        List<String> insertedIds = client.addCourse(new ArrayList<Course>() {{
            add(course);
        }});

        List<Course> courses = client.fetchCourses(insertedIds);
        Assert.assertNotNull(courses);
        log.info("Course created: " + courses);
        return insertedIds;
    }



    private Date getDateFromString(String dateInString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy");
        return sdf.parse(dateInString);
    }

    private Date getTimeFromString(String timeInString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        return sdf.parse(timeInString);
    }

    @Test
    public void testRemoveCourses() throws Exception {
        List<String> insertedIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        long countRemovedEntries = client.removeCourses(insertedIds);
        Assert.assertTrue(countRemovedEntries > 0, "Failed to delete any course");
    }

    @Test
    public void testUpdateCourses() throws Exception {
        List<String> insertedIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        List<Course> courses = client.fetchCourses(insertedIds);
        Assert.assertNotNull(courses);
        for (Course c : courses) {
            c.setAvailabilityStatus(Course.AvailabilityStatus.UNAVAILABLE.getValue());
        }
        log.info("Course modified: " + courses);
        client.updateCourses(courses);
        courses = client.fetchCourses(insertedIds);
        Assert.assertNotNull(courses);
        log.info("Course updated in DB: " + courses);
    }

    @Test
    public void testFetchCourses() throws Exception {
        List<String> insertedIds = testCreateCourse();
        Assert.assertNotNull(insertedIds);
        List<Course> courses = client.fetchCourses(insertedIds);
        Assert.assertNotNull(courses);
    }
}