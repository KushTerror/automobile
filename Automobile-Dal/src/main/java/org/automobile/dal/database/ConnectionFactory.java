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
public class ConnectionFactory {


    private static ConnectionFactory factory;
    private final String dbUser = "niytxhtvigaksk"/*"postgres"*/;
    private final String dbPassword = "kKmQec2QXNfCTS91SrJjpmicMd"/*"godisgr8"*/;
    private final String hostName = "ec2-54-243-47-83.compute-1.amazonaws.com"/*"localhost"*/;
    private final int hostPort = 5432;
    private final String databaseName = "ddcu22i8j1r21o"/*"AutoMobile"*/;
    private Connection mainConnection;

    public static ConnectionFactory getInstance() {
        if (factory == null) {
            factory = new ConnectionFactory();
        }
        return factory;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getHostName() {
        return hostName;
    }

    public int getHostPort() {
        return hostPort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Returns the connection to the POS database
     *
     * @return java.sql.Connection
     * @throws Exception
     */
    private Connection getMainConnection() throws Exception {
        if (mainConnection == null) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Where is your PostgresSQL JDBC Driver? Include in your library path!");
                return null;
            }
            mainConnection = DriverManager.getConnection("jdbc:postgresql://" + hostName + ":" + hostPort + "/" + databaseName, getProperties());
        }
        if (mainConnection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Where is your PostgresSQL JDBC Driver? Include in your library path!");
                return null;
            }
            mainConnection = DriverManager.getConnection("jdbc:postgresql://" + hostName + ":" + hostPort + "/" + databaseName, getProperties());
        }
        return mainConnection;
    }

    private Connection getMainTransConnection() throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgresSQL JDBC Driver? Include in your library path!");
            return null;
        }
        return DriverManager.getConnection("jdbc:postgresql://" + hostName + ":" + hostPort + "/" + databaseName, getProperties());
    }

    /**
     * Returns the connection properties
     *
     * @return java.util.Properties
     * @throws Exception
     */
    private Properties getProperties() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("user", dbUser);
        properties.setProperty("password", dbPassword);
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
