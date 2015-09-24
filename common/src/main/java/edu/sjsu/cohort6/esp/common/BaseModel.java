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


import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Base abstract class for all models.
 *
 * @author rwatsh on 9/23/15.
 */
public abstract class BaseModel {
    private static final Logger log = Logger.getLogger(BaseModel.class.getName());
    public UUID randomUUID() {
        return UUID.randomUUID();
    }

    // note: name does not matter; never auto-detected, need to annotate
    // (also note that formal argument type #1 must be "String"; second one is usually
    //  "Object", but can be something else -- as long as JSON can be bound to that type)
    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        log.warning(MessageFormat.format("Unknown property key {0} value {1}", key, value));
    }
}
