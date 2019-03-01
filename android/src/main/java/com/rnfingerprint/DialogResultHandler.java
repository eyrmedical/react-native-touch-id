package com.rnfingerprint;

import com.facebook.react.bridge.Callback;

public class DialogResultHandler implements BiometricDialog.DialogResultListener {
    private Callback errorCallback;
    private Callback successCallback;

    public DialogResultHandler(Callback reactErrorCallback, Callback reactSuccessCallback) {
        errorCallback = reactErrorCallback;
        successCallback = reactSuccessCallback;
    }

    @Override
    public void onAuthenticated() {
        successCallback.invoke("Successfully authenticated.");
    }

    @Override
    public void onError(String errorString, int errorCode) {
        errorCallback.invoke(errorString, errorCode);
    }

    @Override
    public void onCancelled() {
        errorCallback.invoke("cancelled", BiometricConstants.AUTHENTICATION_CANCELED);
    }
}
