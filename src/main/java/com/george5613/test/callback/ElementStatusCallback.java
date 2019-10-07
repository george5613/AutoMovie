package com.george5613.test.callback;

import io.appium.java_client.android.AndroidElement;

public interface ElementStatusCallback {

    public void onElementTouch(AndroidElement element);

    default public void onTimeout() {
    }
}
