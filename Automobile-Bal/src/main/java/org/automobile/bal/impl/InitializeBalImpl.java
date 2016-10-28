package org.automobile.bal.impl;

import org.automobile.bal.InitializeBal;
import org.automobile.dal.InitializeDal;
import org.automobile.dal.impl.InitializeDalImpl;

/**
 * @author Kushal
 * @Date 27-Oct-16
 */
public class InitializeBalImpl implements InitializeBal {
    private InitializeDal initDal;

    public InitializeBalImpl() {
        this.initDal = new InitializeDalImpl();
    }

    @Override
    public void updateSchemas() throws Exception {
        this.initDal.updateSchemas();
    }

    @Override
    public void updateSchemaTables() throws Exception {
        this.initDal.updateSchemaTables();
    }
}
