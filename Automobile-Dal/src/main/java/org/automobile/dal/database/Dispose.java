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


import org.automobile.dal.log.AutomobileLogger;
import org.automobile.dal.log.impl.AutomobileLoggerImpl;

import java.sql.*;

/**
 * Class to properly dispose SQL objects.
 *
 * @author kushal
 */
public class Dispose {

   private static final AutomobileLogger logger = new AutomobileLoggerImpl(Dispose.class);

   /**
    * Disposes and nullifies the connection, prepared statement and result set
    * passed to the method.
    *
    * @param connection        connection to dispose
    * @param preparedStatement prepared statement to dispose
    * @param resultSet         result set to dispose
    */
   public static void sql(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
      try {
         if (resultSet != null) {
            resultSet.close();
         }
         if (preparedStatement != null) {
            preparedStatement.close();
         }
         if (connection != null) {
            connection.close();
         }
      } catch (SQLException e) {
         logger.error("Error Disposing Sql", e);
      }
   }

   /**
    * Disposes and nullifies the connection, callable statement and result set
    * passed to the method.
    *
    * @param connection        connection to dispose
    * @param callableStatement callable statement to dispose
    * @param resultSet         result set to dispose
    */
   public static void sql(Connection connection, CallableStatement callableStatement, ResultSet resultSet) {
      try {
         if (resultSet != null) {
            resultSet.close();
         }
         if (callableStatement != null) {
            callableStatement.close();
         }
         if (connection != null) {
            connection.close();
         }
      } catch (SQLException e) {
         logger.error("Error Disposing Sql", e);
      }
   }

   /**
    * Disposes and nullifies the connection and callable statement passed to
    * the method
    *
    * @param connection        connection to dispose
    * @param callableStatement callable statement to dispose
    */
   public static void sql(Connection connection, CallableStatement callableStatement) {
      try {
         if (callableStatement != null) {
            callableStatement.close();
         }
         if (connection != null) {
            connection.close();
         }
      } catch (SQLException e) {
         logger.error("Error Disposing Sql", e);
      }
   }

   /**
    * Disposes and nullifies the connection and prepared statement passed to
    * the method
    *
    * @param connection        connection to dispose
    * @param preparedStatement prepared statement to dispose
    */
   public static void sql(Connection connection, PreparedStatement preparedStatement) {
      try {
         if (preparedStatement != null) {
            preparedStatement.close();
         }
         if (connection != null) {
            connection.close();
         }
      } catch (SQLException e) {
         logger.error("Error Disposing Sql", e);
      }
   }

   /**
    * Disposes and nullifies the connection, prepared statement and statement
    * passed to the method
    *
    * @param connection        connection to dispose
    * @param preparedStatement prepared statement to dispose
    * @param statement         statement to dispose
    */
   public static void sql(Connection connection, PreparedStatement preparedStatement, Statement statement) {
      try {
         if (preparedStatement != null) {
            preparedStatement.close();
         }
         if (statement != null) {
            statement.close();
         }
         if (connection != null) {
            connection.close();
         }
      } catch (SQLException e) {
         logger.error("Error Disposing Sql", e);
      }
   }

   /**
    * Disposes and nullifies prepared statement passed to the method
    *
    * @param preparedStatement prepared statement to dispose
    */
   public static void sql(PreparedStatement preparedStatement) {
      try {
         if (preparedStatement != null) {
            preparedStatement.close();
         }
      } catch (SQLException e) {
         logger.error("Error Disposing Sql", e);
      }
   }

   public static void sql(Statement statement) {
      try {
         if (statement != null) {
            statement.close();
         }
      } catch (SQLException e) {
         logger.error("Error Disposing Sql", e);
      }
   }

   /**
    * Disposes and nullifies callable statement passed to the method
    *
    * @param callableStatement callable statement to dispose
    */
   public static void sql(CallableStatement callableStatement) {
      try {
         if (callableStatement != null) {
            callableStatement.close();
         }
      } catch (SQLException e) {
         logger.error("Error Disposing Sql", e);
      }
   }

   /**
    * Disposes and nullifies the connection passed to the method
    *
    * @param connection connection to dispose
    */
   public static void sql(Connection connection) {
      try {
         if (connection != null) {
            connection.close();
         }
      } catch (SQLException e) {
         logger.error("Error Disposing Sql", e);
      }
   }

   /**
    * Disposes and nullifies the connection and result set passed to the method
    *
    * @param connection connection to dispose
    * @param resultSet  result set to dispose
    */
   public static void sql(Connection connection, ResultSet resultSet) {
      try {
         if (resultSet != null) {
            resultSet.close();
         }
         if (connection != null) {
            connection.close();
         }
      } catch (SQLException e) {
         logger.error("Error Disposing Sql", e);
      }
   }

   /**
    * Disposes and nullifies the prepared statement and result passed to the
    * method
    *
    * @param preparedStatement prepared statement to dispose
    * @param resultSet         result set to dispose
    */
   public static void sql(PreparedStatement preparedStatement, ResultSet resultSet) {
      try {
         if (resultSet != null) {
            resultSet.close();
         }
         if (preparedStatement != null) {
            preparedStatement.close();
         }
      } catch (SQLException e) {
         logger.error("Error Disposing Sql", e);
      }
   }

   /**
    * Disposes and nullifies the result set passed to the method
    *
    * @param resultSet result set to dispose
    */
   public static void sql(ResultSet resultSet) {
      try {
         if (resultSet != null) {
            resultSet.close();
         }
      } catch (SQLException e) {
         logger.error("Error Disposing Sql", e);
      }
   }
}
