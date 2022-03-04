#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class Aa_multiplatform_libDeviceInfoCompanion, Aa_multiplatform_libDeviceInfo, UIView, Aa_multiplatform_libKotlinx_serialization_coreSerializersModule, Aa_multiplatform_libKotlinx_serialization_coreSerialKind, Aa_multiplatform_libKotlinNothing;

@protocol Aa_multiplatform_libKotlinx_serialization_coreKSerializer, Aa_multiplatform_libKotlinx_serialization_coreEncoder, Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor, Aa_multiplatform_libKotlinx_serialization_coreSerializationStrategy, Aa_multiplatform_libKotlinx_serialization_coreDecoder, Aa_multiplatform_libKotlinx_serialization_coreDeserializationStrategy, Aa_multiplatform_libKotlinx_serialization_coreCompositeEncoder, Aa_multiplatform_libKotlinAnnotation, Aa_multiplatform_libKotlinx_serialization_coreCompositeDecoder, Aa_multiplatform_libKotlinx_serialization_coreSerializersModuleCollector, Aa_multiplatform_libKotlinKClass, Aa_multiplatform_libKotlinKDeclarationContainer, Aa_multiplatform_libKotlinKAnnotatedElement, Aa_multiplatform_libKotlinKClassifier;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

#pragma push_macro("_Nullable_result")
#if !__has_feature(nullability_nullable_result)
#undef _Nullable_result
#define _Nullable_result _Nullable
#endif

__attribute__((swift_name("KotlinBase")))
@interface Aa_multiplatform_libBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end;

@interface Aa_multiplatform_libBase (Aa_multiplatform_libBaseCopying) <NSCopying>
@end;

__attribute__((swift_name("KotlinMutableSet")))
@interface Aa_multiplatform_libMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end;

__attribute__((swift_name("KotlinMutableDictionary")))
@interface Aa_multiplatform_libMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end;

@interface NSError (NSErrorAa_multiplatform_libKotlinException)
@property (readonly) id _Nullable kotlinException;
@end;

__attribute__((swift_name("KotlinNumber")))
@interface Aa_multiplatform_libNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end;

__attribute__((swift_name("KotlinByte")))
@interface Aa_multiplatform_libByte : Aa_multiplatform_libNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end;

__attribute__((swift_name("KotlinUByte")))
@interface Aa_multiplatform_libUByte : Aa_multiplatform_libNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end;

__attribute__((swift_name("KotlinShort")))
@interface Aa_multiplatform_libShort : Aa_multiplatform_libNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end;

__attribute__((swift_name("KotlinUShort")))
@interface Aa_multiplatform_libUShort : Aa_multiplatform_libNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end;

__attribute__((swift_name("KotlinInt")))
@interface Aa_multiplatform_libInt : Aa_multiplatform_libNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end;

__attribute__((swift_name("KotlinUInt")))
@interface Aa_multiplatform_libUInt : Aa_multiplatform_libNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end;

__attribute__((swift_name("KotlinLong")))
@interface Aa_multiplatform_libLong : Aa_multiplatform_libNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end;

__attribute__((swift_name("KotlinULong")))
@interface Aa_multiplatform_libULong : Aa_multiplatform_libNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end;

__attribute__((swift_name("KotlinFloat")))
@interface Aa_multiplatform_libFloat : Aa_multiplatform_libNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end;

__attribute__((swift_name("KotlinDouble")))
@interface Aa_multiplatform_libDouble : Aa_multiplatform_libNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end;

__attribute__((swift_name("KotlinBoolean")))
@interface Aa_multiplatform_libBoolean : Aa_multiplatform_libNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DeviceInfo")))
@interface Aa_multiplatform_libDeviceInfo : Aa_multiplatform_libBase
- (instancetype)initWithAppId:(NSString *)appId isProd:(BOOL)isProd bundleId:(NSString * _Nullable)bundleId bundleVersion:(NSString * _Nullable)bundleVersion udid:(NSString * _Nullable)udid device:(NSString *)device deviceUdid:(NSString *)deviceUdid os:(NSString *)os osv:(NSString *)osv locale:(NSString * _Nullable)locale timezone:(NSString *)timezone carrier:(NSString *)carrier dw:(int32_t)dw dh:(int32_t)dh density:(int32_t)density isAllowRetargetingEnabled:(BOOL)isAllowRetargetingEnabled sdkVersion:(NSString *)sdkVersion createdAt:(int64_t)createdAt params:(NSDictionary<NSString *, NSString *> *)params __attribute__((swift_name("init(appId:isProd:bundleId:bundleVersion:udid:device:deviceUdid:os:osv:locale:timezone:carrier:dw:dh:density:isAllowRetargetingEnabled:sdkVersion:createdAt:params:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) Aa_multiplatform_libDeviceInfoCompanion *companion __attribute__((swift_name("companion")));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component10 __attribute__((swift_name("component10()")));
- (NSString *)component11 __attribute__((swift_name("component11()")));
- (NSString *)component12 __attribute__((swift_name("component12()")));
- (int32_t)component13 __attribute__((swift_name("component13()")));
- (int32_t)component14 __attribute__((swift_name("component14()")));
- (int32_t)component15 __attribute__((swift_name("component15()")));
- (BOOL)component16 __attribute__((swift_name("component16()")));
- (NSString *)component17 __attribute__((swift_name("component17()")));
- (int64_t)component18 __attribute__((swift_name("component18()")));
- (NSDictionary<NSString *, NSString *> *)component19 __attribute__((swift_name("component19()")));
- (BOOL)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (NSString *)component6 __attribute__((swift_name("component6()")));
- (NSString *)component7 __attribute__((swift_name("component7()")));
- (NSString *)component8 __attribute__((swift_name("component8()")));
- (NSString *)component9 __attribute__((swift_name("component9()")));
- (Aa_multiplatform_libDeviceInfo *)doCopyAppId:(NSString *)appId isProd:(BOOL)isProd bundleId:(NSString * _Nullable)bundleId bundleVersion:(NSString * _Nullable)bundleVersion udid:(NSString * _Nullable)udid device:(NSString *)device deviceUdid:(NSString *)deviceUdid os:(NSString *)os osv:(NSString *)osv locale:(NSString * _Nullable)locale timezone:(NSString *)timezone carrier:(NSString *)carrier dw:(int32_t)dw dh:(int32_t)dh density:(int32_t)density isAllowRetargetingEnabled:(BOOL)isAllowRetargetingEnabled sdkVersion:(NSString *)sdkVersion createdAt:(int64_t)createdAt params:(NSDictionary<NSString *, NSString *> *)params __attribute__((swift_name("doCopy(appId:isProd:bundleId:bundleVersion:udid:device:deviceUdid:os:osv:locale:timezone:carrier:dw:dh:density:isAllowRetargetingEnabled:sdkVersion:createdAt:params:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *appId __attribute__((swift_name("appId")));
@property (readonly) NSString * _Nullable bundleId __attribute__((swift_name("bundleId")));
@property (readonly) NSString * _Nullable bundleVersion __attribute__((swift_name("bundleVersion")));
@property (readonly) NSString *carrier __attribute__((swift_name("carrier")));
@property (readonly) int64_t createdAt __attribute__((swift_name("createdAt")));
@property (readonly) int32_t density __attribute__((swift_name("density")));
@property (readonly) NSString *device __attribute__((swift_name("device")));
@property (readonly) NSString *deviceUdid __attribute__((swift_name("deviceUdid")));
@property (readonly) int32_t dh __attribute__((swift_name("dh")));
@property (readonly) int32_t dw __attribute__((swift_name("dw")));
@property (readonly) BOOL isAllowRetargetingEnabled __attribute__((swift_name("isAllowRetargetingEnabled")));
@property (readonly) BOOL isProd __attribute__((swift_name("isProd")));
@property (readonly) NSString * _Nullable locale __attribute__((swift_name("locale")));
@property (readonly) NSString *os __attribute__((swift_name("os")));
@property (readonly) NSString *osv __attribute__((swift_name("osv")));
@property (readonly) NSDictionary<NSString *, NSString *> *params __attribute__((swift_name("params")));
@property (readonly) NSString *sdkVersion __attribute__((swift_name("sdkVersion")));
@property (readonly) NSString *timezone __attribute__((swift_name("timezone")));
@property (readonly) NSString * _Nullable udid __attribute__((swift_name("udid")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DeviceInfo.Companion")))
@interface Aa_multiplatform_libDeviceInfoCompanion : Aa_multiplatform_libBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) Aa_multiplatform_libDeviceInfoCompanion *shared __attribute__((swift_name("shared")));
- (Aa_multiplatform_libDeviceInfo *)empty __attribute__((swift_name("empty()")));
- (id<Aa_multiplatform_libKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) NSString *OS __attribute__((swift_name("OS")));
@property (readonly) NSString *UNKNOWN_VALUE __attribute__((swift_name("UNKNOWN_VALUE")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Greeting")))
@interface Aa_multiplatform_libGreeting : Aa_multiplatform_libBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (NSString *)greeting __attribute__((swift_name("greeting()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Platform")))
@interface Aa_multiplatform_libPlatform : Aa_multiplatform_libBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@property (readonly) NSString *platform __attribute__((swift_name("platform")));
@end;

__attribute__((unavailable("Kotlin subclass of Objective-C class can't be imported")))
__attribute__((swift_name("ZoneView")))
@interface Aa_multiplatform_libZoneView : NSObject
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ZoneViewKt")))
@interface Aa_multiplatform_libZoneViewKt : Aa_multiplatform_libBase
+ (UIView *)createZoneView __attribute__((swift_name("createZoneView()")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerializationStrategy")))
@protocol Aa_multiplatform_libKotlinx_serialization_coreSerializationStrategy
@required
- (void)serializeEncoder:(id<Aa_multiplatform_libKotlinx_serialization_coreEncoder>)encoder value:(id _Nullable)value __attribute__((swift_name("serialize(encoder:value:)")));
@property (readonly) id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreDeserializationStrategy")))
@protocol Aa_multiplatform_libKotlinx_serialization_coreDeserializationStrategy
@required
- (id _Nullable)deserializeDecoder:(id<Aa_multiplatform_libKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
@property (readonly) id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreKSerializer")))
@protocol Aa_multiplatform_libKotlinx_serialization_coreKSerializer <Aa_multiplatform_libKotlinx_serialization_coreSerializationStrategy, Aa_multiplatform_libKotlinx_serialization_coreDeserializationStrategy>
@required
@end;

__attribute__((swift_name("Kotlinx_serialization_coreEncoder")))
@protocol Aa_multiplatform_libKotlinx_serialization_coreEncoder
@required
- (id<Aa_multiplatform_libKotlinx_serialization_coreCompositeEncoder>)beginCollectionDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor collectionSize:(int32_t)collectionSize __attribute__((swift_name("beginCollection(descriptor:collectionSize:)")));
- (id<Aa_multiplatform_libKotlinx_serialization_coreCompositeEncoder>)beginStructureDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (void)encodeBooleanValue:(BOOL)value __attribute__((swift_name("encodeBoolean(value:)")));
- (void)encodeByteValue:(int8_t)value __attribute__((swift_name("encodeByte(value:)")));
- (void)encodeCharValue:(unichar)value __attribute__((swift_name("encodeChar(value:)")));
- (void)encodeDoubleValue:(double)value __attribute__((swift_name("encodeDouble(value:)")));
- (void)encodeEnumEnumDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)enumDescriptor index:(int32_t)index __attribute__((swift_name("encodeEnum(enumDescriptor:index:)")));
- (void)encodeFloatValue:(float)value __attribute__((swift_name("encodeFloat(value:)")));
- (id<Aa_multiplatform_libKotlinx_serialization_coreEncoder>)encodeInlineInlineDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)inlineDescriptor __attribute__((swift_name("encodeInline(inlineDescriptor:)")));
- (void)encodeIntValue:(int32_t)value __attribute__((swift_name("encodeInt(value:)")));
- (void)encodeLongValue:(int64_t)value __attribute__((swift_name("encodeLong(value:)")));
- (void)encodeNotNullMark __attribute__((swift_name("encodeNotNullMark()")));
- (void)encodeNull __attribute__((swift_name("encodeNull()")));
- (void)encodeNullableSerializableValueSerializer:(id<Aa_multiplatform_libKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableValue(serializer:value:)")));
- (void)encodeSerializableValueSerializer:(id<Aa_multiplatform_libKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableValue(serializer:value:)")));
- (void)encodeShortValue:(int16_t)value __attribute__((swift_name("encodeShort(value:)")));
- (void)encodeStringValue:(NSString *)value __attribute__((swift_name("encodeString(value:)")));
@property (readonly) Aa_multiplatform_libKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerialDescriptor")))
@protocol Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor
@required
- (NSArray<id<Aa_multiplatform_libKotlinAnnotation>> *)getElementAnnotationsIndex:(int32_t)index __attribute__((swift_name("getElementAnnotations(index:)")));
- (id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)getElementDescriptorIndex:(int32_t)index __attribute__((swift_name("getElementDescriptor(index:)")));
- (int32_t)getElementIndexName:(NSString *)name __attribute__((swift_name("getElementIndex(name:)")));
- (NSString *)getElementNameIndex:(int32_t)index __attribute__((swift_name("getElementName(index:)")));
- (BOOL)isElementOptionalIndex:(int32_t)index __attribute__((swift_name("isElementOptional(index:)")));
@property (readonly) NSArray<id<Aa_multiplatform_libKotlinAnnotation>> *annotations __attribute__((swift_name("annotations")));
@property (readonly) int32_t elementsCount __attribute__((swift_name("elementsCount")));
@property (readonly) BOOL isInline __attribute__((swift_name("isInline")));
@property (readonly) BOOL isNullable __attribute__((swift_name("isNullable")));
@property (readonly) Aa_multiplatform_libKotlinx_serialization_coreSerialKind *kind __attribute__((swift_name("kind")));
@property (readonly) NSString *serialName __attribute__((swift_name("serialName")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreDecoder")))
@protocol Aa_multiplatform_libKotlinx_serialization_coreDecoder
@required
- (id<Aa_multiplatform_libKotlinx_serialization_coreCompositeDecoder>)beginStructureDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (BOOL)decodeBoolean __attribute__((swift_name("decodeBoolean()")));
- (int8_t)decodeByte __attribute__((swift_name("decodeByte()")));
- (unichar)decodeChar __attribute__((swift_name("decodeChar()")));
- (double)decodeDouble __attribute__((swift_name("decodeDouble()")));
- (int32_t)decodeEnumEnumDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)enumDescriptor __attribute__((swift_name("decodeEnum(enumDescriptor:)")));
- (float)decodeFloat __attribute__((swift_name("decodeFloat()")));
- (id<Aa_multiplatform_libKotlinx_serialization_coreDecoder>)decodeInlineInlineDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)inlineDescriptor __attribute__((swift_name("decodeInline(inlineDescriptor:)")));
- (int32_t)decodeInt __attribute__((swift_name("decodeInt()")));
- (int64_t)decodeLong __attribute__((swift_name("decodeLong()")));
- (BOOL)decodeNotNullMark __attribute__((swift_name("decodeNotNullMark()")));
- (Aa_multiplatform_libKotlinNothing * _Nullable)decodeNull __attribute__((swift_name("decodeNull()")));
- (id _Nullable)decodeNullableSerializableValueDeserializer:(id<Aa_multiplatform_libKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableValue(deserializer:)")));
- (id _Nullable)decodeSerializableValueDeserializer:(id<Aa_multiplatform_libKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableValue(deserializer:)")));
- (int16_t)decodeShort __attribute__((swift_name("decodeShort()")));
- (NSString *)decodeString __attribute__((swift_name("decodeString()")));
@property (readonly) Aa_multiplatform_libKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreCompositeEncoder")))
@protocol Aa_multiplatform_libKotlinx_serialization_coreCompositeEncoder
@required
- (void)encodeBooleanElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(BOOL)value __attribute__((swift_name("encodeBooleanElement(descriptor:index:value:)")));
- (void)encodeByteElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int8_t)value __attribute__((swift_name("encodeByteElement(descriptor:index:value:)")));
- (void)encodeCharElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(unichar)value __attribute__((swift_name("encodeCharElement(descriptor:index:value:)")));
- (void)encodeDoubleElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(double)value __attribute__((swift_name("encodeDoubleElement(descriptor:index:value:)")));
- (void)encodeFloatElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(float)value __attribute__((swift_name("encodeFloatElement(descriptor:index:value:)")));
- (id<Aa_multiplatform_libKotlinx_serialization_coreEncoder>)encodeInlineElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("encodeInlineElement(descriptor:index:)")));
- (void)encodeIntElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int32_t)value __attribute__((swift_name("encodeIntElement(descriptor:index:value:)")));
- (void)encodeLongElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int64_t)value __attribute__((swift_name("encodeLongElement(descriptor:index:value:)")));
- (void)encodeNullableSerializableElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<Aa_multiplatform_libKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeSerializableElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<Aa_multiplatform_libKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeShortElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int16_t)value __attribute__((swift_name("encodeShortElement(descriptor:index:value:)")));
- (void)encodeStringElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(NSString *)value __attribute__((swift_name("encodeStringElement(descriptor:index:value:)")));
- (void)endStructureDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
- (BOOL)shouldEncodeElementDefaultDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("shouldEncodeElementDefault(descriptor:index:)")));
@property (readonly) Aa_multiplatform_libKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModule")))
@interface Aa_multiplatform_libKotlinx_serialization_coreSerializersModule : Aa_multiplatform_libBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)dumpToCollector:(id<Aa_multiplatform_libKotlinx_serialization_coreSerializersModuleCollector>)collector __attribute__((swift_name("dumpTo(collector:)")));
- (id<Aa_multiplatform_libKotlinx_serialization_coreKSerializer> _Nullable)getContextualKClass:(id<Aa_multiplatform_libKotlinKClass>)kClass typeArgumentsSerializers:(NSArray<id<Aa_multiplatform_libKotlinx_serialization_coreKSerializer>> *)typeArgumentsSerializers __attribute__((swift_name("getContextual(kClass:typeArgumentsSerializers:)")));
- (id<Aa_multiplatform_libKotlinx_serialization_coreSerializationStrategy> _Nullable)getPolymorphicBaseClass:(id<Aa_multiplatform_libKotlinKClass>)baseClass value:(id)value __attribute__((swift_name("getPolymorphic(baseClass:value:)")));
- (id<Aa_multiplatform_libKotlinx_serialization_coreDeserializationStrategy> _Nullable)getPolymorphicBaseClass:(id<Aa_multiplatform_libKotlinKClass>)baseClass serializedClassName:(NSString * _Nullable)serializedClassName __attribute__((swift_name("getPolymorphic(baseClass:serializedClassName:)")));
@end;

__attribute__((swift_name("KotlinAnnotation")))
@protocol Aa_multiplatform_libKotlinAnnotation
@required
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerialKind")))
@interface Aa_multiplatform_libKotlinx_serialization_coreSerialKind : Aa_multiplatform_libBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreCompositeDecoder")))
@protocol Aa_multiplatform_libKotlinx_serialization_coreCompositeDecoder
@required
- (BOOL)decodeBooleanElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeBooleanElement(descriptor:index:)")));
- (int8_t)decodeByteElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeByteElement(descriptor:index:)")));
- (unichar)decodeCharElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeCharElement(descriptor:index:)")));
- (int32_t)decodeCollectionSizeDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeCollectionSize(descriptor:)")));
- (double)decodeDoubleElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeDoubleElement(descriptor:index:)")));
- (int32_t)decodeElementIndexDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeElementIndex(descriptor:)")));
- (float)decodeFloatElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeFloatElement(descriptor:index:)")));
- (id<Aa_multiplatform_libKotlinx_serialization_coreDecoder>)decodeInlineElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeInlineElement(descriptor:index:)")));
- (int32_t)decodeIntElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeIntElement(descriptor:index:)")));
- (int64_t)decodeLongElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeLongElement(descriptor:index:)")));
- (id _Nullable)decodeNullableSerializableElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<Aa_multiplatform_libKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeNullableSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (BOOL)decodeSequentially __attribute__((swift_name("decodeSequentially()")));
- (id _Nullable)decodeSerializableElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<Aa_multiplatform_libKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (int16_t)decodeShortElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeShortElement(descriptor:index:)")));
- (NSString *)decodeStringElementDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeStringElement(descriptor:index:)")));
- (void)endStructureDescriptor:(id<Aa_multiplatform_libKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
@property (readonly) Aa_multiplatform_libKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface Aa_multiplatform_libKotlinNothing : Aa_multiplatform_libBase
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModuleCollector")))
@protocol Aa_multiplatform_libKotlinx_serialization_coreSerializersModuleCollector
@required
- (void)contextualKClass:(id<Aa_multiplatform_libKotlinKClass>)kClass provider:(id<Aa_multiplatform_libKotlinx_serialization_coreKSerializer> (^)(NSArray<id<Aa_multiplatform_libKotlinx_serialization_coreKSerializer>> *))provider __attribute__((swift_name("contextual(kClass:provider:)")));
- (void)contextualKClass:(id<Aa_multiplatform_libKotlinKClass>)kClass serializer:(id<Aa_multiplatform_libKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("contextual(kClass:serializer:)")));
- (void)polymorphicBaseClass:(id<Aa_multiplatform_libKotlinKClass>)baseClass actualClass:(id<Aa_multiplatform_libKotlinKClass>)actualClass actualSerializer:(id<Aa_multiplatform_libKotlinx_serialization_coreKSerializer>)actualSerializer __attribute__((swift_name("polymorphic(baseClass:actualClass:actualSerializer:)")));
- (void)polymorphicDefaultBaseClass:(id<Aa_multiplatform_libKotlinKClass>)baseClass defaultSerializerProvider:(id<Aa_multiplatform_libKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultSerializerProvider __attribute__((swift_name("polymorphicDefault(baseClass:defaultSerializerProvider:)")));
@end;

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol Aa_multiplatform_libKotlinKDeclarationContainer
@required
@end;

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol Aa_multiplatform_libKotlinKAnnotatedElement
@required
@end;

__attribute__((swift_name("KotlinKClassifier")))
@protocol Aa_multiplatform_libKotlinKClassifier
@required
@end;

__attribute__((swift_name("KotlinKClass")))
@protocol Aa_multiplatform_libKotlinKClass <Aa_multiplatform_libKotlinKDeclarationContainer, Aa_multiplatform_libKotlinKAnnotatedElement, Aa_multiplatform_libKotlinKClassifier>
@required
- (BOOL)isInstanceValue:(id _Nullable)value __attribute__((swift_name("isInstance(value:)")));
@property (readonly) NSString * _Nullable qualifiedName __attribute__((swift_name("qualifiedName")));
@property (readonly) NSString * _Nullable simpleName __attribute__((swift_name("simpleName")));
@end;

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
