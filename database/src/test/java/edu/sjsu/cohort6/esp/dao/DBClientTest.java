package edu.sjsu.cohort6.esp.dao;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by rwatsh on 9/13/15.
 */
public class DBClientTest {

    private DBClient client = null;

    @BeforeMethod
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new StudentRegistrationModule());
        client = injector.getInstance(DBClient.class);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testAddStudents() throws Exception {

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