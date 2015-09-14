package edu.sjsu.cohort6.esp.dao;

import com.google.inject.Guice;
import com.google.inject.Inject;
import edu.sjsu.cohort6.esp.dao.mongodb.Student;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by rwatsh on 9/13/15.
 */
public class DBClientTest {

    @Inject
    DBFactory dbFactory;

    public static final String TESTREG = "testreg";
    private static final Logger log = Logger.getLogger(DBClientTest.class.getName());
    private DBClient client;

    @BeforeTest
    public void setUp() throws Exception {
        Guice.createInjector(new DatabaseModule()).injectMembers(this);
        //dbFactory = injector.getInstance(DBFactory.class);

        client = dbFactory.create("localhost", 27017, TESTREG);
        client.dropDB(TESTREG);
    }


    @AfterTest
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testAddStudents() throws Exception {
        client.useDB(TESTREG);
        Student s = new Student("Watsh", "Rajneesh", "watsh.rajneesh@sjsu.edu", "password");
        List<Student> studentList = new ArrayList<>();
        studentList.add(s);
        List<String> insertedIds = client.addStudents(studentList);
        List<Student> students = client.fetchStudents(insertedIds);
        Assert.assertNotNull(students);
        log.info("Student created: " + students);

        client.dropDB(TESTREG);
    }

    @Test
    public void testRemoveStudents() throws Exception {

    }

    @Test
    public void testUpdateStudents() throws Exception {

    }

    @Test
    public void testUpdateStudents1() throws Exception {

    }

    @Test
    public void testFetchStudents() throws Exception {

    }

    @Test
    public void testAddCourse() throws Exception {

    }

    @Test
    public void testRemoveCourses() throws Exception {

    }

    @Test
    public void testUpdateCourses() throws Exception {

    }

    @Test
    public void testFetchCourses() throws Exception {

    }
}