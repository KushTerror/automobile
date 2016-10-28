package org.automobile.dal;

/**
 * @author Kushal
 * @Date 27-Oct-16
 */
public interface InitializeDal {
    void updateSchemas() throws Exception;

    void updateSchemaTables() throws Exception;
}
