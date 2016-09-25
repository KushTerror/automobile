/*
 * Copyright (c) 2015 FC-POS Compulynx LTD.
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

/**
 * Class to maintain the queries required by the password helper
 *
 * @author Kushal
 */
public class QueryConstants {

   /**
    * Query to create the pos_db_owner role
    */
   public static String getCreateDbOwnerRole = "CREATE ROLE pos_db_owner  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION; \n"
      + " COMMENT ON ROLE pos_db_owner IS 'POS Database Owner Group';";
   /**
    * Query to create the pos_read_only role
    */
   public static String getCreateReadOnlyRole = "CREATE ROLE pos_read_only NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION; \n"
      + " COMMENT ON ROLE pos_read_only IS 'POS DataBase Read Only Group';";
   /**
    * Query to create posSupport user
    */
   public static String getCreatePosSupportUser = "CREATE ROLE \"posSupport\" LOGIN \n"
      + "    ENCRYPTED PASSWORD 'md5497193e36e688e20a3028cf189968334' \n"
      + "    NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION; \n"
      + "    GRANT pos_read_only TO \"posSupport\"; \n"
      + "    COMMENT ON ROLE \"posSupport\" IS 'POS Support User Login';";
   /**
    * Query to create pos Schema
    */
   public static String getCreatePosSchema = "CREATE SCHEMA pos AUTHORIZATION pos_db_owner;\n"
      + "    GRANT ALL ON SCHEMA pos TO pos_db_owner WITH GRANT OPTION;\n"
      + "    GRANT USAGE ON SCHEMA pos TO pos_read_only WITH GRANT OPTION;\n"
      + "    REVOKE ALL ON SCHEMA pos FROM public;\n"
      + "    COMMENT ON SCHEMA pos IS 'Pos Scema Owned By Db Owner';";
   /**
    * Query to create the Passwords Table
    */
   public static String getCreateAuthenticationTable = "CREATE TABLE pos.\"M_Authentication\"\n"
      + "    (\n"
      + "      \"Auth_ID\" serial NOT NULL,\n"
      + "      \"Auth_Cur_Pwd\" character varying(500) NOT NULL,\n"
      + "      CONSTRAINT \"PK_Authentications\" PRIMARY KEY (\"Auth_ID\")\n"
      + "    )\n"
      + "    WITH (\n"
      + "      OIDS=FALSE\n"
      + "    );\n"
      + "    ALTER TABLE pos.\"M_Authentication\"\n"
      + "      OWNER TO pos_db_owner;\n"
      + "    GRANT ALL ON TABLE pos.\"M_Authentication\" TO pos_db_owner WITH GRANT OPTION;\n"
      + "    GRANT SELECT ON TABLE pos.\"M_Authentication\" TO pos_read_only WITH GRANT OPTION;\n"
      + "    COMMENT ON TABLE pos.\"M_Authentication\"\n"
      + "      IS 'Table For the Storing the User Password, and SSL Property';\n"
      + "    COMMENT ON COLUMN pos.\"M_Authentication\".\"Auth_ID\" IS 'Authentication ID, Automated Sequence';\n"
      + "    COMMENT ON COLUMN pos.\"M_Authentication\".\"Auth_Cur_Pwd\" IS 'The Current Password For Postgres User';";
   /**
    * Query to check for a particular role
    */
   public static String getCheckRoleName = "Select rolname from pg_roles where lower(rolname) = lower(?)";
   /**
    * Query to check for a particular user
    */
   public static String getCheckUserName = "Select usename from pg_catalog.pg_user where lower(usename) = lower(?)";
   /**
    * Query to check for a particular schema
    */
   public static String getCheckForSchema = "select * from pg_catalog.pg_namespace where lower(nspname) = lower(?)";
   /**
    * Query to check for a particular table
    */
   public static String getCheckForTable = "Select c.relname from pg_catalog.pg_class c join pg_catalog.pg_namespace n on n.oid = c.relnamespace where lower(n.nspname) = 'pos' and lower(c.relname) = lower(?)";
   /**
    * Query to get the database password
    */
   public static String getPassword = "Select \"Auth_Cur_Pwd\" from pos.\"M_Authentication\"";
   /**
    * Query to insert an entry to the passwords table
    */
   public static String getInsertPassword = "Insert Into pos.\"M_Authentication\" (\"Auth_Cur_Pwd\") Values (?)";
   /**
    * Query to update the passwords table
    */
   public static String getUpdatePassword = "Update pos.\"M_Authentication\" set \"Auth_Cur_Pwd\" = ?";
}
