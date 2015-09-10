package edu.sjsu.cohort6.esp.dao;

import java.net.UnknownHostException;

/**
 * Created by rwatsh on 9/8/15.
 */
public class DaoFactory {

    public static DBClient createDBClient(DBType type, String server, int port, String db) throws UnknownHostException {
        switch (type) {
            case MONGODB:
                return null;//MongoDBClient.getInstance(server, port, db);
            default:
                throw new IllegalArgumentException(String.format("Unsupported Database type: %s", new Object[]{type}));
        }
    }
}
