package com.george5613.test.status;

import io.appium.java_client.android.AndroidElement;

public class ElementStatus {

    private AndroidElement mElement;

    public void setElement(AndroidElement element) {
        mElement = element;
    }

    public boolean isFind() {
        return mElement != null;
    }

    public void grant() {
        if (mElement == null) return;
        mElement.click();
    }
}
