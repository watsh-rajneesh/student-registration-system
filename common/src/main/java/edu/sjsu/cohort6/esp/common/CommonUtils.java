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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by rwatsh on 9/22/15.
 */
public class CommonUtils {

    /**
     * Converts a JSON array of objects to List of Java Object types.
     * For example, convert a JSON array of students to List<Student>.
     *
     * @param jsonArrayStr
     * @param clazz
     * @param <T>
     * @return
     * @throws java.io.IOException
     */
    public static <T> List<T> convertJsonArrayToList(String jsonArrayStr, Class<T> clazz) throws java.io.IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonArrayStr,
                TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
    }

    /**
     * Convert JSON string to object.
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T convertJsonToObject(String jsonStr, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonStr, clazz);
    }

    /**
     * Converts object to JSON string representation.
     * It could even be a list of objects which will be converted to JSON array on objects.
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> String convertObjectToJson(T object) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public static String sanitizeIdString(String id) {
        id = id.replaceAll("[\\\"\\']", "");
        return id;
    }
}
