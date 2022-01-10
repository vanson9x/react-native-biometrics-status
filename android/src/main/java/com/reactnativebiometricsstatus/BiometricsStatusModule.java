package com.reactnativebiometricsstatus;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = BiometricsStatusModule.NAME)
public class BiometricsStatusModule extends ReactContextBaseJavaModule {
    public static final String NAME = "BiometricsStatus";

    public BiometricsStatusModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void reset() {
        promise.resolve(true);
    }

    @ReactMethod
    public void hasChanged() {
        promise.resolve(false);
    }

    public static native int nativeReset();
    public static native int nativeHasChanged();
}
