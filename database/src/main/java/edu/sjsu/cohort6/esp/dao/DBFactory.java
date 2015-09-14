package edu.sjsu.cohort6.esp.dao;

import com.google.inject.assistedinject.Assisted;

/**
 * Created by rwatsh on 9/13/15.
 */
public interface DBFactory {
    DBClient create(@Assisted("server") String server, @Assisted("port") int port, @Assisted("dbName") String dbName);
}
