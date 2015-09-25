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

package edu.sjsu.cohort6.esp.service.cli;

import edu.sjsu.cohort6.esp.common.User;
import edu.sjsu.cohort6.esp.dao.DBClient;
import edu.sjsu.cohort6.esp.dao.mongodb.UserDAO;
import edu.sjsu.cohort6.esp.service.StudentRegistrationServiceConfiguration;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rwatsh on 9/24/15.
 */
public class CreateUserCommand extends ConfiguredCommand<StudentRegistrationServiceConfiguration> {
    private static final Logger log = Logger.getLogger(CreateUserCommand.class.getName());

    public CreateUserCommand() {
        super("create-user", "Create an admin user that can access the app");
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);
        subparser.addArgument("-u", "--username")
                .help("admin username");
    }

    @Override
    protected void run(Bootstrap<StudentRegistrationServiceConfiguration> bootstrap,
                       Namespace namespace,
                       StudentRegistrationServiceConfiguration configuration) throws Exception {
        try {
            DBClient dbClient = configuration.getDbConfig().getDbClient();
            User user = new User();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter first name: ");
            user.setFirstName(readInput(scanner));
            System.out.println("Enter last name: ");
            user.setLastName(readInput(scanner));
            System.out.println("Enter email: ");
            user.setEmailId(readInput(scanner));

            // Setting userName to be emailId for now.
            user.setUserName(user.getEmailId());
            user.setToken(user.randomUUID().toString());
            UserDAO userDAO = (UserDAO) dbClient.getDAO(UserDAO.class);
            userDAO.add(new ArrayList<User>(){{
                    add(user);
            }});
            System.out.print("\nUser successfully created:\n");

            StringWriter writer = new StringWriter();
            bootstrap.getObjectMapper().writeValue(writer, user);
            System.out.print(writer.toString());
            System.out.print("\n");
            System.exit(0);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error occurred in creating user ", e);
        }

    }

    private static String readInput(Scanner scanner) {
        if (scanner.hasNext()) {
            String line = scanner.next();
            return line;
        }
        return null;
    }
}
