    /*
     * Licensed to the Apache Software Foundation (ASF) under one or more
     * contributor license agreements.  See the NOTICE file distributed with
     * this work for additional information regarding copyright ownership.
     * The ASF licenses this file to You under the Apache License, Version 2.0
     * (the "License"); you may not use this file except in compliance with
     * the License.  You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.airavata.model.experiment;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A structure holding Quality of Service Parameters.
 * 
 */
@SuppressWarnings("all") public class QualityOfServiceParams implements org.apache.thrift.TBase<QualityOfServiceParams, QualityOfServiceParams._Fields>, java.io.Serializable, Cloneable, Comparable<QualityOfServiceParams> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("QualityOfServiceParams");

  private static final org.apache.thrift.protocol.TField START_EXECUTION_AT_FIELD_DESC = new org.apache.thrift.protocol.TField("startExecutionAt", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField EXECUTE_BEFORE_FIELD_DESC = new org.apache.thrift.protocol.TField("executeBefore", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField NUMBEROF_RETRIES_FIELD_DESC = new org.apache.thrift.protocol.TField("numberofRetries", org.apache.thrift.protocol.TType.I32, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new QualityOfServiceParamsStandardSchemeFactory());
    schemes.put(TupleScheme.class, new QualityOfServiceParamsTupleSchemeFactory());
  }

  private String startExecutionAt; // optional
  private String executeBefore; // optional
  private int numberofRetries; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  @SuppressWarnings("all") public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    START_EXECUTION_AT((short)1, "startExecutionAt"),
    EXECUTE_BEFORE((short)2, "executeBefore"),
    NUMBEROF_RETRIES((short)3, "numberofRetries");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // START_EXECUTION_AT
          return START_EXECUTION_AT;
        case 2: // EXECUTE_BEFORE
          return EXECUTE_BEFORE;
        case 3: // NUMBEROF_RETRIES
          return NUMBEROF_RETRIES;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __NUMBEROFRETRIES_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private _Fields optionals[] = {_Fields.START_EXECUTION_AT,_Fields.EXECUTE_BEFORE,_Fields.NUMBEROF_RETRIES};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.START_EXECUTION_AT, new org.apache.thrift.meta_data.FieldMetaData("startExecutionAt", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.EXECUTE_BEFORE, new org.apache.thrift.meta_data.FieldMetaData("executeBefore", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.NUMBEROF_RETRIES, new org.apache.thrift.meta_data.FieldMetaData("numberofRetries", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(QualityOfServiceParams.class, metaDataMap);
  }

  public QualityOfServiceParams() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public QualityOfServiceParams(QualityOfServiceParams other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetStartExecutionAt()) {
      this.startExecutionAt = other.startExecutionAt;
    }
    if (other.isSetExecuteBefore()) {
      this.executeBefore = other.executeBefore;
    }
    this.numberofRetries = other.numberofRetries;
  }

  public QualityOfServiceParams deepCopy() {
    return new QualityOfServiceParams(this);
  }

  @Override
  public void clear() {
    this.startExecutionAt = null;
    this.executeBefore = null;
    setNumberofRetriesIsSet(false);
    this.numberofRetries = 0;
  }

  public String getStartExecutionAt() {
    return this.startExecutionAt;
  }

  public void setStartExecutionAt(String startExecutionAt) {
    this.startExecutionAt = startExecutionAt;
  }

  public void unsetStartExecutionAt() {
    this.startExecutionAt = null;
  }

  /** Returns true if field startExecutionAt is set (has been assigned a value) and false otherwise */
  public boolean isSetStartExecutionAt() {
    return this.startExecutionAt != null;
  }

  public void setStartExecutionAtIsSet(boolean value) {
    if (!value) {
      this.startExecutionAt = null;
    }
  }

  public String getExecuteBefore() {
    return this.executeBefore;
  }

  public void setExecuteBefore(String executeBefore) {
    this.executeBefore = executeBefore;
  }

  public void unsetExecuteBefore() {
    this.executeBefore = null;
  }

  /** Returns true if field executeBefore is set (has been assigned a value) and false otherwise */
  public boolean isSetExecuteBefore() {
    return this.executeBefore != null;
  }

  public void setExecuteBeforeIsSet(boolean value) {
    if (!value) {
      this.executeBefore = null;
    }
  }

  public int getNumberofRetries() {
    return this.numberofRetries;
  }

  public void setNumberofRetries(int numberofRetries) {
    this.numberofRetries = numberofRetries;
    setNumberofRetriesIsSet(true);
  }

  public void unsetNumberofRetries() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __NUMBEROFRETRIES_ISSET_ID);
  }

  /** Returns true if field numberofRetries is set (has been assigned a value) and false otherwise */
  public boolean isSetNumberofRetries() {
    return EncodingUtils.testBit(__isset_bitfield, __NUMBEROFRETRIES_ISSET_ID);
  }

  public void setNumberofRetriesIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __NUMBEROFRETRIES_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case START_EXECUTION_AT:
      if (value == null) {
        unsetStartExecutionAt();
      } else {
        setStartExecutionAt((String)value);
      }
      break;

    case EXECUTE_BEFORE:
      if (value == null) {
        unsetExecuteBefore();
      } else {
        setExecuteBefore((String)value);
      }
      break;

    case NUMBEROF_RETRIES:
      if (value == null) {
        unsetNumberofRetries();
      } else {
        setNumberofRetries((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case START_EXECUTION_AT:
      return getStartExecutionAt();

    case EXECUTE_BEFORE:
      return getExecuteBefore();

    case NUMBEROF_RETRIES:
      return Integer.valueOf(getNumberofRetries());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case START_EXECUTION_AT:
      return isSetStartExecutionAt();
    case EXECUTE_BEFORE:
      return isSetExecuteBefore();
    case NUMBEROF_RETRIES:
      return isSetNumberofRetries();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof QualityOfServiceParams)
      return this.equals((QualityOfServiceParams)that);
    return false;
  }

  public boolean equals(QualityOfServiceParams that) {
    if (that == null)
      return false;

    boolean this_present_startExecutionAt = true && this.isSetStartExecutionAt();
    boolean that_present_startExecutionAt = true && that.isSetStartExecutionAt();
    if (this_present_startExecutionAt || that_present_startExecutionAt) {
      if (!(this_present_startExecutionAt && that_present_startExecutionAt))
        return false;
      if (!this.startExecutionAt.equals(that.startExecutionAt))
        return false;
    }

    boolean this_present_executeBefore = true && this.isSetExecuteBefore();
    boolean that_present_executeBefore = true && that.isSetExecuteBefore();
    if (this_present_executeBefore || that_present_executeBefore) {
      if (!(this_present_executeBefore && that_present_executeBefore))
        return false;
      if (!this.executeBefore.equals(that.executeBefore))
        return false;
    }

    boolean this_present_numberofRetries = true && this.isSetNumberofRetries();
    boolean that_present_numberofRetries = true && that.isSetNumberofRetries();
    if (this_present_numberofRetries || that_present_numberofRetries) {
      if (!(this_present_numberofRetries && that_present_numberofRetries))
        return false;
      if (this.numberofRetries != that.numberofRetries)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(QualityOfServiceParams other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetStartExecutionAt()).compareTo(other.isSetStartExecutionAt());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStartExecutionAt()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.startExecutionAt, other.startExecutionAt);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetExecuteBefore()).compareTo(other.isSetExecuteBefore());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetExecuteBefore()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.executeBefore, other.executeBefore);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetNumberofRetries()).compareTo(other.isSetNumberofRetries());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNumberofRetries()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.numberofRetries, other.numberofRetries);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("QualityOfServiceParams(");
    boolean first = true;

    if (isSetStartExecutionAt()) {
      sb.append("startExecutionAt:");
      if (this.startExecutionAt == null) {
        sb.append("null");
      } else {
        sb.append(this.startExecutionAt);
      }
      first = false;
    }
    if (isSetExecuteBefore()) {
      if (!first) sb.append(", ");
      sb.append("executeBefore:");
      if (this.executeBefore == null) {
        sb.append("null");
      } else {
        sb.append(this.executeBefore);
      }
      first = false;
    }
    if (isSetNumberofRetries()) {
      if (!first) sb.append(", ");
      sb.append("numberofRetries:");
      sb.append(this.numberofRetries);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class QualityOfServiceParamsStandardSchemeFactory implements SchemeFactory {
    public QualityOfServiceParamsStandardScheme getScheme() {
      return new QualityOfServiceParamsStandardScheme();
    }
  }

  private static class QualityOfServiceParamsStandardScheme extends StandardScheme<QualityOfServiceParams> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, QualityOfServiceParams struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // START_EXECUTION_AT
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.startExecutionAt = iprot.readString();
              struct.setStartExecutionAtIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // EXECUTE_BEFORE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.executeBefore = iprot.readString();
              struct.setExecuteBeforeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // NUMBEROF_RETRIES
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.numberofRetries = iprot.readI32();
              struct.setNumberofRetriesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, QualityOfServiceParams struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.startExecutionAt != null) {
        if (struct.isSetStartExecutionAt()) {
          oprot.writeFieldBegin(START_EXECUTION_AT_FIELD_DESC);
          oprot.writeString(struct.startExecutionAt);
          oprot.writeFieldEnd();
        }
      }
      if (struct.executeBefore != null) {
        if (struct.isSetExecuteBefore()) {
          oprot.writeFieldBegin(EXECUTE_BEFORE_FIELD_DESC);
          oprot.writeString(struct.executeBefore);
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetNumberofRetries()) {
        oprot.writeFieldBegin(NUMBEROF_RETRIES_FIELD_DESC);
        oprot.writeI32(struct.numberofRetries);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class QualityOfServiceParamsTupleSchemeFactory implements SchemeFactory {
    public QualityOfServiceParamsTupleScheme getScheme() {
      return new QualityOfServiceParamsTupleScheme();
    }
  }

  private static class QualityOfServiceParamsTupleScheme extends TupleScheme<QualityOfServiceParams> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, QualityOfServiceParams struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetStartExecutionAt()) {
        optionals.set(0);
      }
      if (struct.isSetExecuteBefore()) {
        optionals.set(1);
      }
      if (struct.isSetNumberofRetries()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetStartExecutionAt()) {
        oprot.writeString(struct.startExecutionAt);
      }
      if (struct.isSetExecuteBefore()) {
        oprot.writeString(struct.executeBefore);
      }
      if (struct.isSetNumberofRetries()) {
        oprot.writeI32(struct.numberofRetries);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, QualityOfServiceParams struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.startExecutionAt = iprot.readString();
        struct.setStartExecutionAtIsSet(true);
      }
      if (incoming.get(1)) {
        struct.executeBefore = iprot.readString();
        struct.setExecuteBeforeIsSet(true);
      }
      if (incoming.get(2)) {
        struct.numberofRetries = iprot.readI32();
        struct.setNumberofRetriesIsSet(true);
      }
    }
  }

}

