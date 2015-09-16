package edu.sjsu.cohort6.esp.service.rest;

import com.google.common.base.Optional;
import edu.sjsu.cohort6.esp.common.Student;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by rwatsh on 9/15/15.
 */
@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource {
    @GET
    public List<Student> getStudent(@QueryParam("id") Optional<String> studentId) {

    }
}
