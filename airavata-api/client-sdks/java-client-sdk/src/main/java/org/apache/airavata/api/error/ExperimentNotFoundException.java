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
package org.apache.airavata.api.error;

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
 * This exception is thrown when a client asks to perform an operation on an experiment that does not exist.
 * 
 * identifier:  A description of the experiment that was not found on the server.
 * 
 * key:  The value passed from the client in the identifier, which was not found.
 */
@SuppressWarnings("all") public class ExperimentNotFoundException extends TException implements org.apache.thrift.TBase<ExperimentNotFoundException, ExperimentNotFoundException._Fields>, java.io.Serializable, Cloneable, Comparable<ExperimentNotFoundException> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ExperimentNotFoundException");

  private static final org.apache.thrift.protocol.TField IDENTIFIER_FIELD_DESC = new org.apache.thrift.protocol.TField("identifier", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField KEY_FIELD_DESC = new org.apache.thrift.protocol.TField("key", org.apache.thrift.protocol.TType.STRING, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ExperimentNotFoundExceptionStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ExperimentNotFoundExceptionTupleSchemeFactory());
  }

  public String identifier; // optional
  public String key; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  @SuppressWarnings("all") public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    IDENTIFIER((short)1, "identifier"),
    KEY((short)2, "key");

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
        case 1: // IDENTIFIER
          return IDENTIFIER;
        case 2: // KEY
          return KEY;
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
  private _Fields optionals[] = {_Fields.IDENTIFIER,_Fields.KEY};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.IDENTIFIER, new org.apache.thrift.meta_data.FieldMetaData("identifier", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.KEY, new org.apache.thrift.meta_data.FieldMetaData("key", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ExperimentNotFoundException.class, metaDataMap);
  }

  public ExperimentNotFoundException() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ExperimentNotFoundException(ExperimentNotFoundException other) {
    if (other.isSetIdentifier()) {
      this.identifier = other.identifier;
    }
    if (other.isSetKey()) {
      this.key = other.key;
    }
  }

  public ExperimentNotFoundException deepCopy() {
    return new ExperimentNotFoundException(this);
  }

  @Override
  public void clear() {
    this.identifier = null;
    this.key = null;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public ExperimentNotFoundException setIdentifier(String identifier) {
    this.identifier = identifier;
    return this;
  }

  public void unsetIdentifier() {
    this.identifier = null;
  }

  /** Returns true if field identifier is set (has been assigned a value) and false otherwise */
  public boolean isSetIdentifier() {
    return this.identifier != null;
  }

  public void setIdentifierIsSet(boolean value) {
    if (!value) {
      this.identifier = null;
    }
  }

  public String getKey() {
    return this.key;
  }

  public ExperimentNotFoundException setKey(String key) {
    this.key = key;
    return this;
  }

  public void unsetKey() {
    this.key = null;
  }

  /** Returns true if field key is set (has been assigned a value) and false otherwise */
  public boolean isSetKey() {
    return this.key != null;
  }

  public void setKeyIsSet(boolean value) {
    if (!value) {
      this.key = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case IDENTIFIER:
      if (value == null) {
        unsetIdentifier();
      } else {
        setIdentifier((String)value);
      }
      break;

    case KEY:
      if (value == null) {
        unsetKey();
      } else {
        setKey((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case IDENTIFIER:
      return getIdentifier();

    case KEY:
      return getKey();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case IDENTIFIER:
      return isSetIdentifier();
    case KEY:
      return isSetKey();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ExperimentNotFoundException)
      return this.equals((ExperimentNotFoundException)that);
    return false;
  }

  public boolean equals(ExperimentNotFoundException that) {
    if (that == null)
      return false;

    boolean this_present_identifier = true && this.isSetIdentifier();
    boolean that_present_identifier = true && that.isSetIdentifier();
    if (this_present_identifier || that_present_identifier) {
      if (!(this_present_identifier && that_present_identifier))
        return false;
      if (!this.identifier.equals(that.identifier))
        return false;
    }

    boolean this_present_key = true && this.isSetKey();
    boolean that_present_key = true && that.isSetKey();
    if (this_present_key || that_present_key) {
      if (!(this_present_key && that_present_key))
        return false;
      if (!this.key.equals(that.key))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(ExperimentNotFoundException other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetIdentifier()).compareTo(other.isSetIdentifier());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetIdentifier()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.identifier, other.identifier);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetKey()).compareTo(other.isSetKey());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetKey()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.key, other.key);
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
    StringBuilder sb = new StringBuilder("ExperimentNotFoundException(");
    boolean first = true;

    if (isSetIdentifier()) {
      sb.append("identifier:");
      if (this.identifier == null) {
        sb.append("null");
      } else {
        sb.append(this.identifier);
      }
      first = false;
    }
    if (isSetKey()) {
      if (!first) sb.append(", ");
      sb.append("key:");
      if (this.key == null) {
        sb.append("null");
      } else {
        sb.append(this.key);
      }
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ExperimentNotFoundExceptionStandardSchemeFactory implements SchemeFactory {
    public ExperimentNotFoundExceptionStandardScheme getScheme() {
      return new ExperimentNotFoundExceptionStandardScheme();
    }
  }

  private static class ExperimentNotFoundExceptionStandardScheme extends StandardScheme<ExperimentNotFoundException> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ExperimentNotFoundException struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // IDENTIFIER
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.identifier = iprot.readString();
              struct.setIdentifierIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // KEY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.key = iprot.readString();
              struct.setKeyIsSet(true);
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

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ExperimentNotFoundException struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.identifier != null) {
        if (struct.isSetIdentifier()) {
          oprot.writeFieldBegin(IDENTIFIER_FIELD_DESC);
          oprot.writeString(struct.identifier);
          oprot.writeFieldEnd();
        }
      }
      if (struct.key != null) {
        if (struct.isSetKey()) {
          oprot.writeFieldBegin(KEY_FIELD_DESC);
          oprot.writeString(struct.key);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ExperimentNotFoundExceptionTupleSchemeFactory implements SchemeFactory {
    public ExperimentNotFoundExceptionTupleScheme getScheme() {
      return new ExperimentNotFoundExceptionTupleScheme();
    }
  }

  private static class ExperimentNotFoundExceptionTupleScheme extends TupleScheme<ExperimentNotFoundException> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ExperimentNotFoundException struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetIdentifier()) {
        optionals.set(0);
      }
      if (struct.isSetKey()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetIdentifier()) {
        oprot.writeString(struct.identifier);
      }
      if (struct.isSetKey()) {
        oprot.writeString(struct.key);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ExperimentNotFoundException struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.identifier = iprot.readString();
        struct.setIdentifierIsSet(true);
      }
      if (incoming.get(1)) {
        struct.key = iprot.readString();
        struct.setKeyIsSet(true);
      }
    }
  }

}

