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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * List all admin users.
 *
 * @author rwatsh on 9/24/15.
 */
public class ListUserCommand extends ConfiguredCommand<StudentRegistrationServiceConfiguration> {

    private static final Logger log = Logger.getLogger(ListUserCommand.class.getName());

    public ListUserCommand() {
        super("list-users", "List all users");
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);
    }

    @Override
    protected void run(Bootstrap<StudentRegistrationServiceConfiguration> bootstrap,
                       Namespace namespace, StudentRegistrationServiceConfiguration configuration) throws Exception {
        try {
            DBClient dbClient = configuration.getDbConfig().getDbClient();
            UserDAO userDAO = (UserDAO) dbClient.getDAO(UserDAO.class);
            List<User> users = userDAO.fetchById(null);

            StringWriter writer = new StringWriter();
            bootstrap.getObjectMapper().writeValue(writer, users);
            System.out.print(writer.toString());
            System.out.print("\n");
            System.exit(0);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error occurred in fetching users ", e);
        }
    }
}
