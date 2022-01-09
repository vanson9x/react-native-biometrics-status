import LocalAuthentication

@objc(BiometricsStatus)
class BiometricsStatus: NSObject {

    @objc static func requiresMainQueueSetup() -> Bool {
        return false
    }

    @objc(reset:withRejecter:)
        func reset(resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
            let context = LAContext()
            let defaults = UserDefaults.standard
            context.canEvaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, error: nil)
            let domainState = context.evaluatedPolicyDomainState
            defaults.set(domainState, forKey: "oldDomainState");
            if(domainState == nil) {resolve(false)}
            else {resolve(true)}
        }

        @available(iOS 11.0, *)
        @objc(hasChanged:withRejecter:)
        func hasChanged(resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
            let context = LAContext()
            let defaults = UserDefaults.standard
            let oldDomainState = defaults.data(forKey: "oldDomainState")
            context.canEvaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, error: nil)
            let domainState = context.evaluatedPolicyDomainState
            if(domainState == nil) {resolve("NOT_AVAILABLE")}
            else if(oldDomainState == nil) {resolve(nil)}
            else {resolve(oldDomainState != domainState)}
        }
}
