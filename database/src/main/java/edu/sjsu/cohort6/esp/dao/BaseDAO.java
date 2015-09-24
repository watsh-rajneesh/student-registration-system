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

package edu.sjsu.cohort6.esp.dao;

import java.util.List;

/**
 * Generic DAO interface for basic CRUD operations on entity T.
 *
 * @author rwatsh on 9/24/15.
 */
public interface BaseDAO<T> {
    List<String> add(List<T> entityList);
    long remove(List<String> entityIdsList);
    void update(List<T> entityList);
    List<T> fetch(List<String> entityIdsList);
}
