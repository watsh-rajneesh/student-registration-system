package edu.sjsu.cohort6.esp.dao;


import com.mongodb.DBObject;

/**
 * Created by rwatsh on 9/7/15.
 */
public interface DataModel {
    public DBObject toDBObject();
    public String getName();
}
