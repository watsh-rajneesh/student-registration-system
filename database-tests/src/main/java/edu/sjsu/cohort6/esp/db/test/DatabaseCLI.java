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

package edu.sjsu.cohort6.esp.db.test;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

/**
 * @author rwatsh on 9/29/15.
 */
public class DatabaseCLI {
    public static void main(String[] args) throws ClassNotFoundException {
        TestListenerAdapter tla = new TestListenerAdapter();

        TestNG test = new TestNG();
        //test.setTestClasses(new Class[]{StudentDAOTest.class, CourseDAOTest.class});
        test.main(args);
        test.addListener(tla);

        test.run();
    }
}
