package org.automobile.bal;

import org.automobile.models.masters.YearMaster;

import java.util.List;

/**
 * @author Kushal
 * @Date 27-Oct-16
 */
public interface MastersBal {
    List<YearMaster> getYearMasters() throws Exception;
}
