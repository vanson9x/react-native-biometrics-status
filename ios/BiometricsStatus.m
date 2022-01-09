#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(BiometricsStatus, NSObject)

RCT_EXTERN_METHOD(reset:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(hasChanged:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

@end
