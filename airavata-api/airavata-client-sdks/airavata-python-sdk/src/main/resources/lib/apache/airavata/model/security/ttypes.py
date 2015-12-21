#
# Autogenerated by Thrift Compiler (0.9.3)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
#  options string: py
#

from thrift.Thrift import TType, TMessageType, TException, TApplicationException

from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol, TProtocol
try:
  from thrift.protocol import fastbinary
except:
  fastbinary = None



class AuthzToken:
  """
  Attributes:
   - accessToken
   - clienKey
   - clientSecret
   - claimsMap
  """

  thrift_spec = (
    None, # 0
    (1, TType.STRING, 'accessToken', None, None, ), # 1
    (2, TType.STRING, 'clienKey', None, None, ), # 2
    (3, TType.STRING, 'clientSecret', None, None, ), # 3
    (4, TType.MAP, 'claimsMap', (TType.STRING,None,TType.STRING,None), None, ), # 4
  )

  def __init__(self, accessToken=None, clienKey=None, clientSecret=None, claimsMap=None,):
    self.accessToken = accessToken
    self.clienKey = clienKey
    self.clientSecret = clientSecret
    self.claimsMap = claimsMap

  def read(self, iprot):
    if iprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None and fastbinary is not None:
      fastbinary.decode_binary(self, iprot.trans, (self.__class__, self.thrift_spec))
      return
    iprot.readStructBegin()
    while True:
      (fname, ftype, fid) = iprot.readFieldBegin()
      if ftype == TType.STOP:
        break
      if fid == 1:
        if ftype == TType.STRING:
          self.accessToken = iprot.readString()
        else:
          iprot.skip(ftype)
      elif fid == 2:
        if ftype == TType.STRING:
          self.clienKey = iprot.readString()
        else:
          iprot.skip(ftype)
      elif fid == 3:
        if ftype == TType.STRING:
          self.clientSecret = iprot.readString()
        else:
          iprot.skip(ftype)
      elif fid == 4:
        if ftype == TType.MAP:
          self.claimsMap = {}
          (_ktype1, _vtype2, _size0 ) = iprot.readMapBegin()
          for _i4 in xrange(_size0):
            _key5 = iprot.readString()
            _val6 = iprot.readString()
            self.claimsMap[_key5] = _val6
          iprot.readMapEnd()
        else:
          iprot.skip(ftype)
      else:
        iprot.skip(ftype)
      iprot.readFieldEnd()
    iprot.readStructEnd()

  def write(self, oprot):
    if oprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and self.thrift_spec is not None and fastbinary is not None:
      oprot.trans.write(fastbinary.encode_binary(self, (self.__class__, self.thrift_spec)))
      return
    oprot.writeStructBegin('AuthzToken')
    if self.accessToken is not None:
      oprot.writeFieldBegin('accessToken', TType.STRING, 1)
      oprot.writeString(self.accessToken)
      oprot.writeFieldEnd()
    if self.clienKey is not None:
      oprot.writeFieldBegin('clienKey', TType.STRING, 2)
      oprot.writeString(self.clienKey)
      oprot.writeFieldEnd()
    if self.clientSecret is not None:
      oprot.writeFieldBegin('clientSecret', TType.STRING, 3)
      oprot.writeString(self.clientSecret)
      oprot.writeFieldEnd()
    if self.claimsMap is not None:
      oprot.writeFieldBegin('claimsMap', TType.MAP, 4)
      oprot.writeMapBegin(TType.STRING, TType.STRING, len(self.claimsMap))
      for kiter7,viter8 in self.claimsMap.items():
        oprot.writeString(kiter7)
        oprot.writeString(viter8)
      oprot.writeMapEnd()
      oprot.writeFieldEnd()
    oprot.writeFieldStop()
    oprot.writeStructEnd()

  def validate(self):
    return


  def __hash__(self):
    value = 17
    value = (value * 31) ^ hash(self.accessToken)
    value = (value * 31) ^ hash(self.clienKey)
    value = (value * 31) ^ hash(self.clientSecret)
    value = (value * 31) ^ hash(self.claimsMap)
    return value

  def __repr__(self):
    L = ['%s=%r' % (key, value)
      for key, value in self.__dict__.iteritems()]
    return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

  def __eq__(self, other):
    return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

  def __ne__(self, other):
    return not (self == other)
