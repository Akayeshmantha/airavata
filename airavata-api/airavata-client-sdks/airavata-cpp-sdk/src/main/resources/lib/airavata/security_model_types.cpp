/**
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
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#include "security_model_types.h"

#include <algorithm>
#include <ostream>

#include <thrift/TToString.h>

namespace apache { namespace airavata { namespace model { namespace security {


AuthzToken::~AuthzToken() throw() {
}


void AuthzToken::__set_accessToken(const std::string& val) {
  this->accessToken = val;
__isset.accessToken = true;
}

void AuthzToken::__set_clienKey(const std::string& val) {
  this->clienKey = val;
__isset.clienKey = true;
}

void AuthzToken::__set_clientSecret(const std::string& val) {
  this->clientSecret = val;
__isset.clientSecret = true;
}

void AuthzToken::__set_claimsMap(const std::map<std::string, std::string> & val) {
  this->claimsMap = val;
__isset.claimsMap = true;
}

uint32_t AuthzToken::read(::apache::thrift::protocol::TProtocol* iprot) {

  apache::thrift::protocol::TInputRecursionTracker tracker(*iprot);
  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->accessToken);
          this->__isset.accessToken = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->clienKey);
          this->__isset.clienKey = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->clientSecret);
          this->__isset.clientSecret = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 4:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->claimsMap.clear();
            uint32_t _size0;
            ::apache::thrift::protocol::TType _ktype1;
            ::apache::thrift::protocol::TType _vtype2;
            xfer += iprot->readMapBegin(_ktype1, _vtype2, _size0);
            uint32_t _i4;
            for (_i4 = 0; _i4 < _size0; ++_i4)
            {
              std::string _key5;
              xfer += iprot->readString(_key5);
              std::string& _val6 = this->claimsMap[_key5];
              xfer += iprot->readString(_val6);
            }
            xfer += iprot->readMapEnd();
          }
          this->__isset.claimsMap = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t AuthzToken::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  apache::thrift::protocol::TOutputRecursionTracker tracker(*oprot);
  xfer += oprot->writeStructBegin("AuthzToken");

  if (this->__isset.accessToken) {
    xfer += oprot->writeFieldBegin("accessToken", ::apache::thrift::protocol::T_STRING, 1);
    xfer += oprot->writeString(this->accessToken);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.clienKey) {
    xfer += oprot->writeFieldBegin("clienKey", ::apache::thrift::protocol::T_STRING, 2);
    xfer += oprot->writeString(this->clienKey);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.clientSecret) {
    xfer += oprot->writeFieldBegin("clientSecret", ::apache::thrift::protocol::T_STRING, 3);
    xfer += oprot->writeString(this->clientSecret);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.claimsMap) {
    xfer += oprot->writeFieldBegin("claimsMap", ::apache::thrift::protocol::T_MAP, 4);
    {
      xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->claimsMap.size()));
      std::map<std::string, std::string> ::const_iterator _iter7;
      for (_iter7 = this->claimsMap.begin(); _iter7 != this->claimsMap.end(); ++_iter7)
      {
        xfer += oprot->writeString(_iter7->first);
        xfer += oprot->writeString(_iter7->second);
      }
      xfer += oprot->writeMapEnd();
    }
    xfer += oprot->writeFieldEnd();
  }
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

void swap(AuthzToken &a, AuthzToken &b) {
  using ::std::swap;
  swap(a.accessToken, b.accessToken);
  swap(a.clienKey, b.clienKey);
  swap(a.clientSecret, b.clientSecret);
  swap(a.claimsMap, b.claimsMap);
  swap(a.__isset, b.__isset);
}

AuthzToken::AuthzToken(const AuthzToken& other8) {
  accessToken = other8.accessToken;
  clienKey = other8.clienKey;
  clientSecret = other8.clientSecret;
  claimsMap = other8.claimsMap;
  __isset = other8.__isset;
}
AuthzToken& AuthzToken::operator=(const AuthzToken& other9) {
  accessToken = other9.accessToken;
  clienKey = other9.clienKey;
  clientSecret = other9.clientSecret;
  claimsMap = other9.claimsMap;
  __isset = other9.__isset;
  return *this;
}
void AuthzToken::printTo(std::ostream& out) const {
  using ::apache::thrift::to_string;
  out << "AuthzToken(";
  out << "accessToken="; (__isset.accessToken ? (out << to_string(accessToken)) : (out << "<null>"));
  out << ", " << "clienKey="; (__isset.clienKey ? (out << to_string(clienKey)) : (out << "<null>"));
  out << ", " << "clientSecret="; (__isset.clientSecret ? (out << to_string(clientSecret)) : (out << "<null>"));
  out << ", " << "claimsMap="; (__isset.claimsMap ? (out << to_string(claimsMap)) : (out << "<null>"));
  out << ")";
}

}}}} // namespace
