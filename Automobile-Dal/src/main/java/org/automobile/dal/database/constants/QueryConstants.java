package org.automobile.dal.database.constants;

/**
 * @author Kushal
 * @Date 27-Oct-16
 */
public class QueryConstants {

    public static String getCreateSchema = "CREATE SCHEMA auto\n" +
            "  AUTHORIZATION {@User};";

    public static String getCheckForSchema = "select * from pg_catalog.pg_namespace where lower(nspname) = lower(?)";

    public static String getYearsMasters = "SELECT year_id, year FROM auto.mst_years;";

}
