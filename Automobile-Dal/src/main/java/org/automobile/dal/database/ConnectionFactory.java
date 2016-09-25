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

import org.automobile.dal.database.enums.DatabaseUsers;
import org.automobile.dal.database.enums.Databases;
import org.automobile.dal.database.security.PasswordHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Class for Getting postgres connections
 *
 * @author kushal
 */
public class ConnectionFactory {

    public static PasswordHelper getPasswordHelper() {
        return PasswordHelper.getInstance();
    }

    public static void disposePasswordHelper() {
        PasswordHelper.disposeInstance();
    }

    /**
     * Returns the connection to the postgres database
     *
     * @return java.sql.Connection
     * @throws Exception
     */
    public static Connection getMasterDbConnection() throws Exception {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                getProperties());
    }

    /**
     * Returns the connection to the POS database
     *
     * @return java.sql.Connection
     * @throws Exception
     */
    public static Connection getPosDbConnection() throws Exception {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/automobile",
                getProperties());
    }

    /**
     * Returns the connection to the required database
     *
     * @param databases database to connect to
     * @return java.sql.Connection
     * @throws Exception
     */
    public static Connection getDatabaseConnection(Databases databases) throws Exception {
        switch (databases) {
            case AUTOMOBILE:
                return getPosDbConnection();
            case MASTER:
                return getMasterDbConnection();
            default:
                return getPosDbConnection();
        }
    }

    /**
     * Returns the connection properties
     *
     * @return java.util.Properties
     * @throws Exception
     */
    private static Properties getProperties() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        properties.setProperty("password", PasswordHelper.getInstance().getPassword(DatabaseUsers.postgres));
        if (PasswordHelper.getInstance().isSupportSSL()) {
            properties.setProperty("ssl", "true");
        }
        return properties;
    }
}
