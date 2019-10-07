package com.george5613.test.app;

import com.george5613.test.app.global.Context;
import com.george5613.test.base.BaseAction;
import com.george5613.test.helper.ElementHelper;
import com.george5613.test.helper.UiSelectorHelper;
import io.appium.java_client.android.AndroidElement;

import static com.george5613.test.app.global.Constant.TAOPP.SPLASH_ACTIVITY;

public class SplashAction extends BaseAction {

    private static final String TAG = SPLASH_ACTIVITY;

    public SplashAction(Context context) {
        super(context);
    }

    @Override
    public String getTag() {
        return TAG;
    }

}
