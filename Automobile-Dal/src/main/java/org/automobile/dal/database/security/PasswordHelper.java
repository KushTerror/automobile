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

package org.automobile.dal.database.security;


import org.automobile.dal.database.Dispose;
import org.automobile.dal.database.enums.DatabaseUsers;
import org.automobile.dal.log.AutomobileLogger;
import org.automobile.dal.log.impl.AutomobileLoggerImpl;
import org.automobile.dal.security.AES;
import org.automobile.dal.security.RandomString;

import java.sql.*;
import java.util.Properties;

/**
 * Class to Manage Database Passwords
 *
 * @author kushal
 */
public class PasswordHelper {

    /**
     * Singleton Instance
     */
    private static PasswordHelper passwordHelper;
    private final AutomobileLogger logger;
    /**
     * AES Crypto instance
     */
    private final AES aes;
    /**
     * Random Password generator instance
     */
    private final RandomString randomString;
    /**
     * Variable to hold the connection
     */
    private Connection connection;
    /**
     * Variable to indicate whether the connection support SSL or Not
     */
    private boolean supportSSL;
    /**
     * Variable to indicate whether the posSupport Read Only User exists or not
     */
    private boolean readOnlyUserExists;

    /**
     * Variable to indicate whether the Authentication Table has been Created or
     * Not
     */
    private boolean posTableExists;
    /**
     * Maintains the sessions super user password
     */
    private String superUserPassword;
    /**
     * Maintains the sessions super user Previous password
     */
    private String superUserPreviousPassword;

    /**
     * Initializes the class setting SSL supported to true, and posSupport user exists to
     * true
     */
    public PasswordHelper() {
        aes = new AES();
        randomString = new RandomString(60);
        logger = new AutomobileLoggerImpl(getClass());
        try {
            supportSSL = true;
            readOnlyUserExists = true;
            posTableExists = false;
            superUserPassword = "godisgr8";
            superUserPreviousPassword = aes.encrypt("godisgr8");
        } catch (Exception ex) {
            logger.error("Error Initializing Password Helper Instance", ex);
        }
    }

    /**
     * Returns the singleton instance
     *
     * @return PasswordHelper instance
     */
    public static PasswordHelper getInstance() {
        if (passwordHelper == null) {
            passwordHelper = new PasswordHelper();
        }
        return passwordHelper;
    }

    /**
     * Method to dispose the singleton instance
     */
    public static void disposeInstance() {
        if (passwordHelper != null) {
            passwordHelper.dispose();
        }
        passwordHelper = null;
    }

    /**
     * Method to return the SSL status
     *
     * @return true if SSL or false
     */
    public boolean isSupportSSL() {
        return supportSSL;
    }

    /**
     * Method to dispose the read only connection
     */
    public void dispose() {
        Dispose.sql(connection);
    }

    /**
     * Returns the current database server password.
     *
     * @param databaseUsers Password for which user postgres or posSupport
     * @return The password
     */
    public String getPassword(DatabaseUsers databaseUsers) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getValidConnection();
                if (connection == null) {
                    logger.error("Fatal Error, Null Connection Returned");
                    return null;
                }
                if (!readOnlyUserExists) {
                    //Check and Create Roles
                    //Check Pos_Db_Owner Role
                    if (!checkAndUpdateDatabase(connection, QueryConstants.getCheckRoleName, QueryConstants.getCreateDbOwnerRole, "pos_db_owner")) {
                        return null;
                    }
                    //Check pos_read_only Role
                    if (!checkAndUpdateDatabase(connection, QueryConstants.getCheckRoleName, QueryConstants.getCreateReadOnlyRole, "pos_read_only")) {
                        return null;
                    }
                    //Check and Create Users
                    if (!checkAndUpdateDatabase(connection, QueryConstants.getCheckUserName, QueryConstants.getCreatePosSupportUser, "posSupport")) {
                        return null;
                    }
                    //Dispose Connection
                    Dispose.sql(connection);
                    //Get Read Only Connection
                    readOnlyUserExists = true;
                    connection = getValidConnection();
                    //Ensuring the Connection is Not Null
                    if (connection == null) {
                        logger.error("Fatal Error, Null Connection Returned");
                        return null;
                    }
                }
            }
            //Query Passwords Db and Return Password
            return getPasswordFromDatabase(connection);
        } catch (Exception e) {
            logger.error("Error Getting Password", e);
            return null;
        }
    }

    /**
     * Updates the database password
     *
     * @return true if the password is updated
     */
    public boolean updatePassword() {
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        Connection updateConnection = null;
        String newPassword;
        try {
            newPassword = randomString.nextString();
            if (!newPassword.equalsIgnoreCase("")) {
                updateConnection = getSuperUserConnection();
                assert updateConnection != null;
                updateConnection.setAutoCommit(false);
                preparedStatement = updateConnection.prepareStatement(QueryConstants.getUpdatePassword);
                preparedStatement.setString(1, aes.encrypt(newPassword));
                preparedStatement.executeUpdate();
                statement = updateConnection.createStatement();
                statement.executeUpdate(String.format("Alter user postgres with password '%s'", newPassword));
                updateConnection.commit();
                superUserPassword = newPassword;
                superUserPreviousPassword = aes.encrypt(newPassword);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Error Updating Super User Password", e);
            if (updateConnection != null) {
                try {
                    updateConnection.rollback();
                } catch (SQLException ex) {
                    logger.error("Error Rolling Back Password Update", ex);
                }
            }
            return false;
        } finally {
            Dispose.sql(updateConnection, preparedStatement, statement);
        }
    }

    /**
     * Method to check and update database using super user connection
     *
     * @param superUserConnection Connection to use
     * @param checkQuery          Check query
     * @param executeQuery        execute query
     * @param parameterValue      parameter value
     * @return true on success
     */
    private boolean checkAndUpdateDatabase(Connection superUserConnection, String checkQuery, String executeQuery, String parameterValue) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            boolean exists = false;
            preparedStatement = superUserConnection.prepareStatement(checkQuery);
            preparedStatement.setString(1, parameterValue);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                exists = true;
            }
            if (!exists) {
                Dispose.sql(preparedStatement, resultSet);
                preparedStatement = superUserConnection.prepareStatement(executeQuery);
                preparedStatement.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            logger.error("Error Creating Super User", e);
            return false;
        } finally {
            Dispose.sql(preparedStatement, resultSet);
        }
    }

    /**
     * Method to use read only connection to check database and super user to
     * execute update
     *
     * @param readOnlyConnection Read only user Connection
     * @param checkQuery         Check Query
     * @param executeQuery       Execute Query
     * @param parameterValue     ParameterValue value
     * @return true on success
     */
    private boolean readOnlyCheckAndCreateDb(Connection readOnlyConnection, String checkQuery, String executeQuery, String parameterValue) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection superUserConnection = null;
        try {
            boolean b = false;
            preparedStatement = readOnlyConnection.prepareStatement(checkQuery);
            preparedStatement.setString(1, parameterValue);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                b = true;
            }
            if (!b) {
                Dispose.sql(preparedStatement, resultSet);
                superUserConnection = getSuperUserConnection();
                assert superUserConnection != null;
                preparedStatement = superUserConnection.prepareStatement(executeQuery);
                preparedStatement.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            logger.error("Error Creating ReadOnly User", e);
            return false;
        } finally {
            Dispose.sql(superUserConnection, preparedStatement, resultSet);
        }
    }

    /**
     * Method to get the password from the database
     *
     * @param passwordConnection Connection to use
     * @return password
     */
    private String getPasswordFromDatabase(Connection passwordConnection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection superUserConnection = null;
        try {
            if (!posTableExists) {
                //Check and Create Pos Schema
                if (!readOnlyCheckAndCreateDb(passwordConnection, QueryConstants.getCheckForSchema, QueryConstants.getCreatePosSchema, "pos")) {
                    return null;
                }
                //Check and Create Authentication Table
                if (!readOnlyCheckAndCreateDb(passwordConnection, QueryConstants.getCheckForTable, QueryConstants.getCreateAuthenticationTable, "M_Authentication")) {
                    return null;
                }
                posTableExists = true;
            }
            preparedStatement = passwordConnection.prepareStatement(QueryConstants.getPassword);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String dbP = resultSet.getString("Auth_Cur_Pwd");
                if (superUserPreviousPassword == null ? dbP != null : !superUserPreviousPassword.equals(dbP)) {
                    superUserPassword = aes.decrypt(dbP);
                }
                return superUserPassword;
            } else {
                Dispose.sql(preparedStatement, resultSet);
                superUserConnection = getSuperUserConnection();
                assert superUserConnection != null;
                preparedStatement = superUserConnection.prepareStatement(QueryConstants.getInsertPassword);
                preparedStatement.setString(1, aes.encrypt("godisgr8"));
                preparedStatement.executeUpdate();
                return "godisgr8";
            }
        } catch (Exception e) {
            logger.error("Error Retrieving password", e);
            return null;
        } finally {
            Dispose.sql(superUserConnection, preparedStatement, resultSet);
        }
    }

    /**
     * Method to get valid connection
     *
     * @return Connection
     */
    private Connection getValidConnection() {
        Connection validConnection;
        validConnection = getReadOnlyConnection();
        if (validConnection == null) {
            validConnection = getSuperUserConnection();
        }
        return validConnection;
    }

    /**
     * Method to get Super User Connection
     *
     * @return Connection
     */
    private Connection getSuperUserConnection() {
        Connection superUserConnection;
        Properties properties;
        if (supportSSL) {
            try {
                properties = new Properties();
                properties.setProperty("user", "postgres");
                properties.setProperty("password", superUserPassword);
                properties.setProperty("supportSSL", "true");
                superUserConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", properties);
                return superUserConnection;
            } catch (Exception e) {
                if (e.getMessage().equalsIgnoreCase("The server does not support SSL.")) {
                    supportSSL = false;
                }
                logger.error("Secured Super User Connection Failed", e);
            }
        }
        if (!supportSSL) {
            try {
                properties = new Properties();
                properties.setProperty("user", "postgres");
                properties.setProperty("password", superUserPassword);
                superUserConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", properties);
                return superUserConnection;
            } catch (Exception e) {
                logger.error("Unsecured Super User Connection Failed", e);
            }
        }
        return null;
    }

    /**
     * Method to Return the read only connection for the user
     *
     * @return Connection
     */
    private Connection getReadOnlyConnection() {
        Connection readOnlyConnection;
        Properties properties;
        if (supportSSL && readOnlyUserExists) {
            try {
                properties = new Properties();
                properties.setProperty("user", "posSupport");
                properties.setProperty("password", "support");
                properties.setProperty("supportSSL", "true");
                readOnlyConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", properties);
                return readOnlyConnection;
            } catch (Exception e) {
                logger.error("Secure Read Only Connection Failed", e);
                if (e.getMessage().equalsIgnoreCase("The server does not support SSL.")) {
                    supportSSL = false;
                } else if (e.getMessage().equalsIgnoreCase("FATAL: password authentication failed for user \"posSupport\"")) {
                    readOnlyUserExists = false;
                }
            }
        }
        if ((!supportSSL) && readOnlyUserExists) {
            try {
                properties = new Properties();
                properties.setProperty("user", "posSupport");
                properties.setProperty("password", "support");
                readOnlyConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", properties);
                return readOnlyConnection;
            } catch (Exception e) {
                logger.error("Unsecured Read Only Connection Failed", e);
                if (e.getMessage().equalsIgnoreCase("FATAL: password authentication failed for user \"posSupport\"")) {
                    readOnlyUserExists = false;
                }
            }
        }
        return null;
    }
}
