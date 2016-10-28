package org.automobile.dal.impl;

import org.automobile.dal.MastersDal;
import org.automobile.dal.database.Operations;
import org.automobile.dal.database.constants.QueryConstants;
import org.automobile.dal.database.enums.QueryTypes;
import org.automobile.dal.database.models.Parameter;
import org.automobile.models.masters.YearMaster;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kushal
 * @Date 27-Oct-16
 */
public class MastersDalImpl implements MastersDal {

    @Override
    public List<YearMaster> getYearMasters() throws Exception {
        Operations instance = null;
        ResultSet set = null;
        try {
            instance = Operations.getInstance();
            List<YearMaster> yearMasters = new ArrayList<>();

            set = instance.getResultSet(new Parameter(QueryConstants.getYearsMasters, QueryTypes.QUERY));
            while (set.next()) {
                yearMasters.add(new YearMaster(set.getInt("year_id"), set.getInt("year")));
            }
            return yearMasters;
        } finally {
            if (instance != null) {
                if (set != null) {
                    instance.dispose(set);
                } else {
                    instance.dispose();
                }
            }
        }
    }
}
