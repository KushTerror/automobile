/*
 * Copyright (c) 2016 FC-POS Compulynx LTD.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are NOT permitted.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL COMPULYNX LTD BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.automobile.dal.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Class for Getting postgres connections
 *
 * @author kushal
 */
class ConnectionFactory {


    private static ConnectionFactory factory;
    private Connection mainConnection;

    public static ConnectionFactory getInstance() {
        if (factory == null) {
            factory = new ConnectionFactory();
        }
        return factory;
    }

    /**
     * Returns the connection to the POS database
     *
     * @return java.sql.Connection
     * @throws Exception
     */
    private Connection getMainConnection() throws Exception {
        if (mainConnection == null) {
            mainConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/corePos",
                    getProperties());
        }
        if (mainConnection.isClosed()) {
            mainConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/corePos",
                    getProperties());
        }
        return mainConnection;
    }

    private Connection getMainTransConnection() throws Exception {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/corePos",
                getProperties());
    }

    /**
     * Returns the connection properties
     *
     * @return java.util.Properties
     * @throws Exception
     */
    private Properties getProperties() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        properties.setProperty("password", "godisgr8");
        return properties;
    }

    /**
     * Returns the connection to the required database
     *
     * @return java.sql.Connection
     * @throws Exception
     */
    Connection getDatabaseConnection() throws Exception {
        return getMainConnection();
    }

    Connection getDatabaseTransConnection() throws Exception {
        return getMainTransConnection();
    }
}
