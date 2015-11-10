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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by rwatsh on 9/22/15.
 */
public class CommonUtils {

    private static final Logger log = Logger.getLogger(CommonUtils.class.getName());
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
        //jsonArrayStr = removeIdField(jsonArrayStr);
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
        //jsonStr = removeIdField(jsonStr);
        return mapper.readValue(jsonStr, clazz);
    }

    /**
     * This method was needed to parse entity attribute out of other "unwanted" properties that Jersey response was
     * appending. With this approach any unsturctured JSON data can be parsed into a generic JSONObject type and
     * only the desired field of a known name can be singled out of it. Once we single out the entity piece from the
     * response we use Jackson again to parse entity to our known type (Student, Course or User).
     *
     * @param response      jersey response
     * @param clazz         the known type
     * @return
     * @throws IOException
     * @throws org.json.simple.parser.ParseException
     */
    public static <T> T convertJsonToObject(Response response, Class<T> clazz) throws IOException, org.json.simple.parser.ParseException {
        String json = getJsonFromResponse(response);
        return convertJsonToObject(json, clazz);
    }

    /**
     * This method parses the generic jersey response to a generic JSONObject and singles out the "entity" piece
     * from the generic response.
     *
     * @param response
     * @return
     * @throws org.json.simple.parser.ParseException
     */
    private static String getJsonFromResponse(Response response) throws org.json.simple.parser.ParseException {
        String c = response.readEntity(String.class);
        log.info(c.toString());
        JSONParser parser=new JSONParser();
        JSONObject json = (JSONObject) parser.parse(c);
        JSONObject jsonObject = (JSONObject) json.get("entity");
        return jsonObject.toJSONString();
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

    private static String generateMD5Hash(String plaintext) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    /**
     * Converts string form of date in a Date object in UTC timezone.
     *
     * @param dateInString
     * @return
     * @throws ParseException
     */
    public static Date getDateFromString(String dateInString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy HH:mm");
        return sdf.parse(dateInString);
    }
}
