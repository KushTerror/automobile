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

/**
 * Class to maintain SQL ParameterValue for the query
 *
 * @author Kushal
 */
public class ParameterValue {

   /**
    * Column Type, Use Java.Sql.Types for the Static Data Types
    */
   protected int dataType;

   /**
    * The Value to update into the database
    */
   protected Object value;
   /**
    * The ParameterValue Index in the Statement
    */
   protected int index;

   /**
    * Initalizes the entity with default values
    *
    * @param dataType Integer type form Java.Sql.Types
    * @param value    Value of the parameter
    * @param index    ParameterValue index
    */
   public ParameterValue(int dataType, Object value, int index) {
      this.dataType = dataType;
      this.value = value;
      this.index = index;
   }

   /**
    * Gets the Data type of the parameter
    *
    * @return integer
    */
   public int getDataType() {
      return dataType;
   }

   /**
    * Set the data type of the parameter
    *
    * @param dataType Static Integer from Java.Sql.Types Class
    */
   public void setDataType(int dataType) {
      this.dataType = dataType;
   }

   /**
    * Get the value to update into the database
    *
    * @return Object
    */
   public Object getValue() {
      return value;
   }

   /**
    * Set the Value to update into the database
    *
    * @param value Value to set
    */
   public void setValue(Object value) {
      this.value = value;
   }

   /**
    * Get the index of the parameter
    *
    * @return parameter index
    */
   public int getIndex() {
      return index;
   }

   /**
    * Set the parameter index
    *
    * @param index index of the parameter
    */
   public void setIndex(int index) {
      this.index = index;
   }
}
