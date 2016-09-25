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

package org.automobile.dal.database.models;


import org.automobile.dal.database.enums.QueryTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for SQL Query parameters
 *
 * @author Kushal
 */
public class Parameter {

    /**
     * Variable for list of parameters and their values
     */
    List<ParameterValue> parameterValues;
    /**
     * Variable for query string
     */
    private String queryString;
    /**
     * Variable for query type
     */
    private QueryTypes queryTypes;

    /**
     * Initalizes the entity with the queryString and type
     *
     * @param queryString Query string
     * @param queryTypes  Type of the queryString
     */
    public Parameter(String queryString, QueryTypes queryTypes) {
        this.queryString = queryString;
        this.queryTypes = queryTypes;
        this.parameterValues = new ArrayList<>();
    }

    /**
     * Initializes the entity with the queryString, type and the list of parameters
     *
     * @param queryString     Query String
     * @param queryTypes      Type of the Query
     * @param parameterValues List of parameters and their values
     */
    public Parameter(String queryString, QueryTypes queryTypes, List<ParameterValue> parameterValues) {
        this.queryString = queryString;
        this.queryTypes = queryTypes;
        this.parameterValues = parameterValues;
    }

    /**
     * Initialize the entity with list of values only
     *
     * @param parameterValues List of parameters and their values
     */
    public Parameter(List<ParameterValue> parameterValues) {
        this.parameterValues = parameterValues;
    }

    /**
     * Query Specified
     *
     * @return queryString string
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     * Sets the queryString to execute
     *
     * @param queryString query String to execute
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /**
     * Get the queryString type
     *
     * @return queryString type
     */
    public QueryTypes getQueryTypes() {
        return queryTypes;
    }

    /**
     * Sets the queryString type
     *
     * @param queryTypes type of the queryString
     */
    public void setQueryTypes(QueryTypes queryTypes) {
        this.queryTypes = queryTypes;
    }

    /**
     * Gets the list of parameter values for the specified queryString
     *
     * @return ParameterValue List
     */
    public List<ParameterValue> getParameterValues() {
        return parameterValues;
    }

    /**
     * Sets the parameter list of the queryString specified
     *
     * @param parameterValues List of ParameterValue and their values
     */
    public void setParameterValues(List<ParameterValue> parameterValues) {
        this.parameterValues = parameterValues;
    }

    /**
     * Adds A parameter to the parameter List
     *
     * @param dataType Integer Value from Java.Sql.Types
     * @param value    Value of the parameter
     * @param index    Index of the ParameterValue
     */
    public void addParameter(int dataType, Object value, int index) {
        this.parameterValues.add(new ParameterValue(dataType, value, index));
    }

}
