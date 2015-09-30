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

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @author rwatsh on 9/29/15.
 */
public class DBTestListener extends TestListenerAdapter {

    private static final Logger log = Logger.getLogger(DBTestListener.class.getName());

    @Override
    public void onStart(ITestContext context) {
        log.info(context.toString());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Starting test ==>> " +
                " for method: " + result.getTestClass() + "#" + result.getMethod() + "\n" +
                " with params:  " + Arrays.toString(result.getParameters()));


        Scanner scanner = new Scanner(System.in);
        System.out.println("Proceed to next test? (Y/n) ");
        String input = scanner.nextLine();
        log.info(input);
        if (input.toLowerCase().startsWith("n")) {
            log.info("Ending tests");
            System.exit(0);
        }
    }

}
