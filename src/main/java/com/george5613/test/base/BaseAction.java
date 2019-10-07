package com.george5613.test.base;

import com.george5613.test.app.global.Context;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public abstract class BaseAction implements IStepAction {

    protected Context mContext;

    public BaseAction(Context context) {
        mContext = context;
    }

    protected AndroidDriver<AndroidElement> getDriver() {
        return mContext.getDriver();
    }

    public abstract String getTag();

    @Override
    public void doStep() {

    }
}
