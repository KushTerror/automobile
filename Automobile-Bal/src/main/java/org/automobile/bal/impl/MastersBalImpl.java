package org.automobile.bal.impl;

import org.automobile.bal.MastersBal;
import org.automobile.dal.MastersDal;
import org.automobile.models.masters.YearMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kushal
 * @Date 27-Oct-16
 */
@Component
public class MastersBalImpl implements MastersBal {
    @Autowired
    private MastersDal mastersDal;

    @Override
    public List<YearMaster> getYearMasters() throws Exception {
        return mastersDal.getYearMasters();
    }
}
