package com.rnfingerprint;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;


import androidx.fragment.app.FragmentActivity;

public class BiometricDialogModule extends ReactContextBaseJavaModule {


    private KeyguardManager keyguardManager;

    BiometricDialogModule(final ReactApplicationContext reactContext) {
        super(reactContext);
    }

    private KeyguardManager getKeyguardManager() {
        if (keyguardManager != null) {
            return keyguardManager;
        }
        final Activity activity = getCurrentActivity();
        if (activity == null) {
            return null;
        }

        keyguardManager = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);

        return keyguardManager;
    }

    @Override
    public String getName() {
        return "FingerprintAuth";
    }

    @ReactMethod
    public void isSupported(final Callback reactErrorCallback, final Callback reactSuccessCallback) {
        final Activity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }

        int result = BiometricConstants.IS_SUPPORTED; //TODO add isSupported
        if (result == BiometricConstants.IS_SUPPORTED) {
            reactSuccessCallback.invoke("Is supported.");
        } else {
            reactErrorCallback.invoke("Not supported.", result);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @ReactMethod
    public void authenticate(final String reason, final ReadableMap authConfig, final Callback reactErrorCallback, final Callback reactSuccessCallback) {
        final DialogResultHandler drh = new DialogResultHandler(reactErrorCallback, reactSuccessCallback);


        BiometricDialog biometricDialog = new BiometricDialog((FragmentActivity) getCurrentActivity(), drh, authConfig);
        biometricDialog.show();
    }

}
