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

import org.automobile.dal.database.enums.QueryTypes;
import org.automobile.dal.database.models.Parameter;
import org.automobile.dal.database.models.ParameterValue;

import java.sql.*;
import java.util.List;

/**
 * Class For Database related posOps
 *
 * @author Kushal
 */
public class Operations {
    /**
     * Global SQL Connection Instance
     */
    private Connection connection;
    /**
     * Global Prepared Statement Instance
     */
    private PreparedStatement preparedStatement;
    /**
     * Global Statement Instance
     */
    private Statement statement;
    /**
     * Global Callable Statement instance
     */
    private CallableStatement callableStatement;

    /**
     * Initalizes the class by instantiating the Logger Entity
     *
     * @throws Exception
     */
    public Operations() throws Exception {
        initConnection();
    }

    /**
     * Method to return the singleton instance of the posOps class
     *
     * @return org.compulynx.core.cmn.utl.database.posOps
     * @throws Exception
     */
    public static Operations getInstance() throws Exception {
        return new Operations();
    }

    /**
     * Method to Initalize the database connection
     *
     * @throws Exception
     */
    private void initConnection() throws Exception {
        this.connection = ConnectionFactory.getInstance().getDatabaseConnection();
        if (connection == null || connection.isClosed()) {
            throw new NullPointerException("Null Connection, can not proceed");
        }
    }

    /**
     * Disposes the Global Variables of the class
     *
     * @param rs The result set to dispose
     */
    public void dispose(ResultSet rs) {
        Dispose.sql(rs);
        Dispose.sql(preparedStatement);
        Dispose.sql(callableStatement);
        Dispose.sql(statement);
    }

    /**
     * Disposes the Global Variables of the class
     */
    public void dispose() {
        Dispose.sql(preparedStatement);
        Dispose.sql(statement);
        Dispose.sql(callableStatement);
    }

    /**
     * Dispose all global variables including the connection
     */
    public void disposeAll() {
        Dispose.sql(preparedStatement);
        Dispose.sql(callableStatement);
        Dispose.sql(statement);
        Dispose.sql(connection);
    }

    /**
     * Sets the parameters to the specified Prepared statement
     *
     * @param preparedStatement Prepared statement to set parameters
     * @param parameterValues   List Of ParameterValue values to be set
     * @throws Exception
     */
    private void setParameters(PreparedStatement preparedStatement, List<ParameterValue> parameterValues) throws Exception {
        if (parameterValues == null) {
            return;
        }
        if (parameterValues.isEmpty()) {
            return;
        }
        for (int x = 0; x <= parameterValues.size() - 1; x++) {
            preparedStatement.setObject(parameterValues.get(x).getIndex(), parameterValues.get(x).getValue(), parameterValues.get(x).getDataType());
        }
    }

    /**
     * Sets the parameters to the specified Callable statement
     *
     * @param callableStatement Callable statement to set parameters
     * @param parameterValues   List Of ParameterValue values to be set
     * @throws Exception
     */
    private void setParameters(CallableStatement callableStatement, List<ParameterValue> parameterValues) throws Exception {
        if (parameterValues == null) {
            return;
        }
        if (parameterValues.isEmpty()) {
            return;
        }

        for (int x = 0; x <= parameterValues.size() - 1; x++) {
            callableStatement.setObject(parameterValues.get(x).getIndex(), parameterValues.get(x).getValue(), parameterValues.get(x).getDataType());
        }
    }

    /**
     * Executes the Query on the database specified and returns true if the
     * record exists
     *
     * @param parameter SQL ParameterValue and query to execute
     * @return true if the record exists
     * @throws Exception
     */
    private boolean recordExistsQuery(Parameter parameter) throws Exception {
        preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if (connection == null || connection.isClosed()) {
                initConnection();
            }
            preparedStatement = connection.prepareStatement(parameter.getQueryString());
            setParameters(preparedStatement, parameter.getParameterValues());
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } finally {
            Dispose.sql(resultSet);
        }
    }

    /**
     * Executes the Stored Procedure on the database specified and returns true
     * if the record exists
     *
     * @param parameter SQL ParameterValue and Stored procedure to execute
     * @return true if the record exists
     * @throws Exception
     */
    private boolean recordExistsSp(Parameter parameter) throws Exception {
        preparedStatement = null;
        ResultSet rs = null;
        try {
            if (connection == null || connection.isClosed()) {
                initConnection();
            }
            callableStatement = connection.prepareCall(parameter.getQueryString());
            setParameters(callableStatement, parameter.getParameterValues());
            rs = callableStatement.executeQuery();
            return rs.next();
        } finally {
            Dispose.sql(rs);
        }
    }

    /**
     * Executes the Query on the database specified and returns true if the
     * record is updated successfully
     *
     * @param parameter SQL ParameterValue and query to execute
     * @throws Exception
     */
    public void executeUpdateQuery(Parameter parameter) throws Exception {
        preparedStatement = null;
        if (connection == null || connection.isClosed()) {
            initConnection();
        }
       /*System.out.println(parameter.getQueryString());*/
        preparedStatement = connection.prepareStatement(parameter.getQueryString());
        setParameters(preparedStatement, parameter.getParameterValues());
        preparedStatement.executeUpdate();
    }

    public int executeUpdateQueryAndGetAffectedCount(Parameter parameter) throws Exception {
        preparedStatement = null;
        if (connection == null || connection.isClosed()) {
            initConnection();
        }
        preparedStatement = connection.prepareStatement(parameter.getQueryString());
        setParameters(preparedStatement, parameter.getParameterValues());
        return preparedStatement.executeUpdate();
    }

    public ResultSet executeUpdateQueryAndReturnId(Parameter parameter) throws Exception {
        preparedStatement = null;
        if (connection == null || connection.isClosed()) {
            initConnection();
        }
        preparedStatement = connection.prepareStatement(parameter.getQueryString());
        setParameters(preparedStatement, parameter.getParameterValues());
        //System.out.println("------"+preparedStatement);
        return preparedStatement.executeQuery();
    }

    /**
     * Initiates the prepared statement for a single query
     *
     * @param parameter Sql ParameterValue to prepare
     * @throws Exception
     */
    public void prepareStatement(Parameter parameter) throws Exception {
        preparedStatement = null;
        if (connection == null || connection.isClosed()) {
            initConnection();
        }
        preparedStatement = connection.prepareStatement(parameter.getQueryString());
    }

    /**
     * Executes the Prepared statement initialized on using the prepareStatement method
     *
     * @param parameterValues ParameterValue to assign
     * @throws Exception
     */
    public void executeQuery(List<ParameterValue> parameterValues) throws Exception {
        if (preparedStatement == null) {
            throw new NullPointerException("Prepared Statement is not initialized. Please Call prepareStatement before calling this method");
        }
        setParameters(preparedStatement, parameterValues);
        preparedStatement.executeUpdate();
    }

    /**
     * Executes the Stored Procedure on the database specified and returns true
     * if the record is updated successfully
     *
     * @param parameter SQL ParameterValue and Stored procedure to execute
     * @throws Exception
     */
    public void executeStoredProcedure(Parameter parameter) throws Exception {
        preparedStatement = null;
        if (connection == null || connection.isClosed()) {
            initConnection();
        }
        callableStatement = connection.prepareCall(parameter.getQueryString());
        setParameters(callableStatement, parameter.getParameterValues());
        int affectedCount = callableStatement.executeUpdate();
        if (affectedCount == 0) {
            throw new Exception("No Records Updated");
        }
    }

    public int executeStoredProcedureAndGetAffectedCount(Parameter parameter) throws Exception {
        preparedStatement = null;
        if (connection == null || connection.isClosed()) {
            initConnection();
        }
        callableStatement = connection.prepareCall(parameter.getQueryString());
        setParameters(callableStatement, parameter.getParameterValues());
        return callableStatement.executeUpdate();
    }

    public ResultSet executeStoredProcedureAndReturnId(Parameter parameter) throws Exception {
        preparedStatement = null;
        if (connection == null || connection.isClosed()) {
            initConnection();
        }
        callableStatement = connection.prepareCall(parameter.getQueryString());
        setParameters(callableStatement, parameter.getParameterValues());
        return callableStatement.executeQuery();
    }

    /**
     * Execute query and return results
     *
     * @param parameter the parameters containing the query to the data being sought
     * @return java.sql.ResultSet
     * @throws Exception
     */
    private ResultSet getQueryResultSet(Parameter parameter) throws Exception {
        preparedStatement = null;
        if (connection == null || connection.isClosed()) {
            initConnection();
        }
        preparedStatement = connection.prepareStatement(parameter.getQueryString());
        setParameters(preparedStatement, parameter.getParameterValues());
        return preparedStatement.executeQuery();
    }

    /**
     * Execute Stored procedure and return results
     *
     * @param p the parameters containing the stored procedure to the data being
     *          sought
     * @return java.sql.ResultSet
     * @throws Exception
     */
    private ResultSet getStoredProcedureResultSet(Parameter p) throws Exception {
        callableStatement = null;
        try {
            if (connection == null || connection.isClosed()) {
                initConnection();
            }
            callableStatement = connection.prepareCall(p.getQueryString());
            setParameters(callableStatement, p.getParameterValues());
            return callableStatement.executeQuery();
        } finally {
            Dispose.sql(callableStatement);
        }
    }

    /**
     * Executes the query specified and returns the first row first column
     * result
     *
     * @param p Sql parameter to execute
     * @return Object containing the data
     * @throws Exception
     */
    private Object getQueryFirstValue(Parameter p) throws Exception {
        preparedStatement = null;
        ResultSet rs = null;
        try {
            if (connection == null || connection.isClosed()) {
                initConnection();
            }
            preparedStatement = connection.prepareStatement(p.getQueryString());
            setParameters(preparedStatement, p.getParameterValues());
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getObject(1);
            }
            return null;
        } finally {
            Dispose.sql(preparedStatement, rs);
        }
    }

    /**
     * Executes the stored procedure an returns the first column first row data
     *
     * @param p Sql parameter to execute
     * @return Object
     * @throws Exception
     */
    private Object getStoredProcedureFirstValue(Parameter p) throws Exception {
        callableStatement = null;
        ResultSet rs = null;
        try {
            if (connection == null || connection.isClosed()) {
                initConnection();
            }
            callableStatement = connection.prepareCall(p.getQueryString());
            setParameters(callableStatement, p.getParameterValues());
            rs = callableStatement.executeQuery();
            if (rs.next()) {
                return rs.getObject(1);
            }
            return null;
        } finally {
            Dispose.sql(preparedStatement, rs);
        }
    }

    /**
     * Execute statement
     *
     * @param parameter Sql ParameterValue
     * @throws Exception
     */
    public void executeQuery(Parameter parameter) throws Exception {
        if (parameter.getQueryTypes() == QueryTypes.QUERY) {
            executeUpdateQuery(parameter);
        } else {
            executeStoredProcedure(parameter);
        }
    }

    public int executeQueryAndGetAffectedCount(Parameter parameter) throws Exception {
        if (parameter.getQueryTypes() == QueryTypes.QUERY) {
            return executeUpdateQueryAndGetAffectedCount(parameter);
        } else {
            return executeStoredProcedureAndGetAffectedCount(parameter);
        }
    }

    public ResultSet executeQueryAndReturnId(Parameter parameter) throws Exception {
        if (parameter.getQueryTypes() == QueryTypes.QUERY) {
            return executeUpdateQueryAndReturnId(parameter);
        } else {
            return executeStoredProcedureAndReturnId(parameter);
        }
    }

    /**
     * Check if a record exists
     *
     * @param parameter Check Sql ParameterValue
     * @return true if data exists
     * @throws Exception
     */
    public boolean recordExists(Parameter parameter) throws Exception {
        if (parameter.getQueryTypes() == QueryTypes.QUERY) {
            return recordExistsQuery(parameter);
        } else {
            return recordExistsSp(parameter);
        }
    }

    /**
     * Executes the Query on the POS Database
     *
     * @param parameter SQL parameters specification with the query and the required
     *                  parameters
     * @return A Result set with the data queried
     * @throws Exception
     */
    public ResultSet getResultSet(Parameter parameter) throws Exception {
        if (parameter.getQueryTypes() == QueryTypes.QUERY) {
            return getQueryResultSet(parameter);
        } else {
            return getStoredProcedureResultSet(parameter);
        }
    }

    /**
     * Executes the Query on the specified Database
     *
     * @param parameter SQL parameters specification with the query and the required
     *                  parameters
     * @return the data queried
     * @throws Exception
     */
    public Object getFirstValue(Parameter parameter) throws Exception {
        if (parameter.getQueryTypes() == QueryTypes.QUERY) {
            return getQueryFirstValue(parameter);
        } else {
            return getStoredProcedureFirstValue(parameter);
        }
    }

    /**
     * Queries the postgres Database for the existence of the record using the
     * check parameters, and if the record doesn't exist, executes the execute
     * parameter
     *
     * @param checkParameter   SQL parameters specification with the Check query and the
     *                         required parameters
     * @param executeParameter SQL parameters specification with the Update query and the
     *                         required parameters
     * @throws Exception
     */
    public void verifyAndExecute(Parameter checkParameter, Parameter executeParameter) throws Exception {
        if (!recordExists(checkParameter)) {
            executeQuery(executeParameter);
        }
    }

    public void executeBatchStatment(List<String> queries) throws Exception {
        statement = null;
        if (connection == null || connection.isClosed()) {
            initConnection();
        }

        statement = connection.createStatement();
        //Stopwatch stopwatch = Stopwatch.createStarted();
        for (String query : queries) {
            statement.addBatch(query);
        }
        //stopwatch.stop();
        //System.out.println("\u001B[35m" + "Batch Statement Adding completed in (Seconds): " + stopwatch.elapsed(TimeUnit.SECONDS) + "\u001B[0m");
        //stopwatch = Stopwatch.createStarted();
        statement.executeBatch();
        //stopwatch.stop();
        //System.out.println("\u001B[35m" + "Batch Statement Execution completed in (Seconds): " + stopwatch.elapsed(TimeUnit.SECONDS) + "\u001B[0m");
    }

    public Connection beginTransaction() throws Exception {
        Connection transConnection = ConnectionFactory.getInstance().getDatabaseConnection();
        transConnection.setAutoCommit(false); //Begin Transaction Block
        return transConnection;
    }

    public void commitTransaction(Connection transConnection) throws Exception {
        transConnection.commit();
        transConnection.setAutoCommit(true); //Begin Transaction Block
        Dispose.sql(preparedStatement);
        Dispose.sql(statement);
        Dispose.sql(callableStatement);
        Dispose.sql(transConnection);
    }

    public void rollBackTransaction(Connection transConnection) throws Exception {
        transConnection.rollback();
        transConnection.setAutoCommit(true); //Begin Transaction Block
        Dispose.sql(preparedStatement);
        Dispose.sql(statement);
        Dispose.sql(callableStatement);
        Dispose.sql(transConnection);
    }
}

