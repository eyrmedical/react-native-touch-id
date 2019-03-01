package com.rnfingerprint;

import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.react.bridge.ReadableMap;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

class BiometricDialog {

    private static final String TAG = "BiometricDialog";
    private final DialogResultListener callback;
    private FragmentActivity activity;
    private String title;
    private String description;
    private String negativeButtonText;

    BiometricDialog(FragmentActivity activity, BiometricDialog.DialogResultListener callback, final ReadableMap config) {
        this.activity = activity;
        this.callback = callback;
        this.title = config.getString("title");
        this.negativeButtonText = config.getString("cancelText");
        this.description = config.getString("sensorDescription");
    }


    void show() {

        Executor executor = Executors.newSingleThreadExecutor();

        final BiometricPrompt biometricPrompt = new BiometricPrompt(this.activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    Log.i(TAG, "User cancelled");
                    BiometricDialog.this.callback.onCancelled();
                } else {
                    Log.i(TAG, "Unknown error with code: " + errorCode + "desc: " + String.valueOf(errString));
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                Log.i(TAG, "onAuthenticationSucceeded");
                BiometricDialog.this.callback.onAuthenticated();
            }

            @Override
            public void onAuthenticationFailed() {
                Log.i(TAG, "onAuthenticationFailed");
                BiometricDialog.this.callback.onError("auth failed", 0);

            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(this.title)
                .setDescription(this.description)
                .setNegativeButtonText(this.negativeButtonText)
                .build();

        biometricPrompt.authenticate(promptInfo);

    }

    public interface DialogResultListener {
        void onAuthenticated();

        void onError(String errorString, int errorCode);

        void onCancelled();
    }


}
