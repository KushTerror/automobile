package org.automobile.dal.impl;

import org.automobile.dal.InitializeDal;
import org.automobile.dal.database.ConnectionFactory;
import org.automobile.dal.database.Operations;
import org.automobile.dal.database.constants.QueryConstants;
import org.automobile.dal.database.enums.QueryTypes;
import org.automobile.dal.database.models.Parameter;
import org.automobile.dal.database.models.ParameterValue;
import org.automobile.dal.log.AutomobileLogger;
import org.automobile.dal.log.impl.AutomobileLoggerImpl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Types;
import java.util.ArrayList;

/**
 * @author Kushal
 * @Date 27-Oct-16
 */
public class InitializeDalImpl implements InitializeDal {

    private AutomobileLogger logger;

    public InitializeDalImpl() {
        this.logger = new AutomobileLoggerImpl(getClass());
    }

    @Override
    public void updateSchemas() throws Exception {
        Operations instance = null;
        try {
            instance = Operations.getInstance();
            if (!instance.recordExists(new Parameter(QueryConstants.getCheckForSchema, QueryTypes.QUERY, new ArrayList<ParameterValue>() {{
                add(new ParameterValue(Types.VARCHAR, "auto", 1));
            }}))) {
                instance.executeQuery(new Parameter(QueryConstants.getCreateSchema.replace("{@User}", ConnectionFactory.getInstance().getDbUser()), QueryTypes.QUERY));
            }
        } finally {
            if (instance != null) {
                instance.dispose();
            }
        }
    }

    @Override
    public void updateSchemaTables() throws Exception {
        Operations instance = null;
        StringBuilder stringBuilder;
        try {
            try (InputStream ins = InitializeDalImpl.class.getResourceAsStream("/Automobile.db")) {
                if (ins != null) {
                    stringBuilder = new StringBuilder();
                    instance = Operations.getInstance();
                    try (Reader r = new InputStreamReader(ins, "UTF-8")) {
                        try (BufferedReader br = new BufferedReader(r)) {
                            String s;
                            while ((s = br.readLine()) != null) {
                                if (s.trim().equalsIgnoreCase("go")) {
                                    logger.info("Execute:\n" + stringBuilder.toString());
                                    instance.executeQuery(new Parameter(stringBuilder.toString(), QueryTypes.QUERY));
                                    stringBuilder = new StringBuilder();
                                } else {
                                    if (!s.equalsIgnoreCase("")) {
                                        stringBuilder.append(s.replace("{@User}", ConnectionFactory.getInstance().getDbUser())).append("\n");
                                    }
                                }
                            }
                        }
                    }
                    instance.dispose();
                }
            }
        } finally {
            if (instance != null) {
                instance.dispose();
            }
        }
    }
}
