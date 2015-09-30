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

package edu.sjsu.cohort6.esp.service.rest;

import edu.sjsu.cohort6.esp.common.Student;
import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.service.rest.exception.InternalErrorException;
import edu.sjsu.cohort6.esp.service.rest.exception.ResourceNotFoundException;
import io.dropwizard.auth.Auth;
import org.bson.types.ObjectId;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import javax.validation.Valid;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by rwatsh on 9/15/15.
 */
@Path(EndpointUtils.ENDPOINT_ROOT + "/students")
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource extends BaseResource<Student> {

    private static final Logger log = Logger.getLogger(StudentResource.class.getName());

    public JacksonDBCollection<Student, String> getJacksonDBCollection() {
        return JacksonDBCollection.wrap(studentDAO.getCollection(), Student.class, String.class);
    }


    public StudentResource(DBClient client) {
        super(client);
    }


    @Override
    public Student create(@Auth User user, @Valid Student student, @Context UriInfo info) {
        try {
            /*log.info(MessageFormat.format("Creating student with user name {0}", user.getUserName()));
            List<Student> studentList = new ArrayList<>();
            studentList.add(student);
            List<String> studentIds = studentDAO.add(studentList);
            log.info(MessageFormat.format("Created student with user name {0}", user.getUserName()));
            return Response.ok().build();*/
            WriteResult<Student, String> result = getJacksonDBCollection().insert(student);
            return result.getSavedObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new edu.sjsu.cohort6.esp.service.rest.exception.BadRequestException();
        }
    }

    @Override
    public List<Student> list(@Auth User user) {
        /*List<String> studentIds = new ArrayList<>();
        List<Student> studentList = studentDAO.fetchById(studentIds);
        return studentList;*/
        return getJacksonDBCollection().find().toArray();
    }

    @Override
    public Student retrieve(@Auth User user, @PathParam("id") String studentId) throws ResourceNotFoundException {
        /*List<String> studentIds = getStudentIdsList(studentId);
        List<Student> studentList = studentDAO.fetchById(studentIds);
        if (studentList != null && !studentList.isEmpty()) {
            return studentList.get(0);
        } else {
            throw new ResourceNotFoundException();
        }*/
        return getJacksonDBCollection().findOneById(studentId);
    }

    private List<String> getStudentIdsList(String studentId) {
        List<String> studentIds = new ArrayList<>();

        if (studentId != null && !studentId.isEmpty()) {
            studentIds.add(studentId);
        } else {
            studentIds = null;
        }
        return studentIds;
    }

    private List<Student> getStudentsList(Student student) {
        List<Student> students = new ArrayList<>();

        if (student != null) {
            students.add(student);
        } else {
            students = null;
        }
        return students;
    }

    @Override
    public Student update(@Auth User user, @PathParam("id") String id, @Valid Student student) throws ResourceNotFoundException {
        student.setId(new ObjectId(id));
        try {
            /*studentDAO.update(getStudentsList(student));
            List<Student> studentList = studentDAO.fetchById(getStudentIdsList(id));
            if (studentList != null && !studentList.isEmpty()) {
                return studentList.get(0);
            }
            throw new ResourceNotFoundException();*/
            WriteResult<Student, String> writeResult = getJacksonDBCollection().updateById(id, student);
            return writeResult.getSavedObject();
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public int delete(@Auth User user, @PathParam("id") String id) throws ResourceNotFoundException {
        try {
            /*studentDAO.remove(getStudentIdsList(id));
            return Response.ok().build();*/
            WriteResult<Student, String> writeResult = getJacksonDBCollection().removeById(id);
            return writeResult.getN();
        } catch (Exception e) {
            throw new ResourceNotFoundException();
        }
    }
}
