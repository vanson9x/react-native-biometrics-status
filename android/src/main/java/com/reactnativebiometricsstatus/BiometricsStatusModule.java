package com.reactnativebiometricsstatus;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@RequiresApi(api = Build.VERSION_CODES.M)
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
    public void reset(Promise promise) {
        promise.resolve(true);
    }

    @ReactMethod
    public void hasChanged(Promise pm) {
        SecretKey secretKey = getSecretKey();
        if (secretKey == null) {
            generateSecretKey();
            pm.resolve(false);
        } else {
            try {
                Cipher cipher = getCipher();
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                pm.resolve(false);
            } catch (KeyPermanentlyInvalidatedException e) {
                pm.resolve(true);
            } catch (InvalidKeyException e) {
                Log.e(getName(), e.toString());
                pm.resolve("Error: " + e.toString());
            }
        }
    }

    private void generateSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                keyGenerator.init(new KeyGenParameterSpec.Builder(
                        "fingerPrintKey",
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setUserAuthenticationRequired(true)
                        .setInvalidatedByBiometricEnrollment(true)
                        .build());
            }
            keyGenerator.generateKey();
        } catch (Exception e) {
            Log.e(getName(), "Exception method generateSecretKey");
            Log.e(getName(), e.toString());
        }
    }

    private SecretKey getSecretKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            return (SecretKey) keyStore.getKey("fingerPrintKey", null);
        } catch (Exception e) {
            Log.e(getName(), "Exception method getSecretKey");
            Log.e(getName(), e.toString());
            return null;
        }
    }

    private Cipher getCipher() {
        try {
            return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (Exception e) {
            Log.e(getName(), "Exception method getCipher");
            Log.e(getName(), e.toString());
            return null;
        }
    }

    public static native int nativeReset();

    public static native int nativeHasChanged();
}
