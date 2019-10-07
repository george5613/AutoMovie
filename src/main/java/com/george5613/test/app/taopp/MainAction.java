package com.george5613.test.app.taopp;

import com.george5613.test.app.global.Constant;
import com.george5613.test.app.global.Context;
import com.george5613.test.base.BaseAction;
import com.george5613.test.callback.ElementStatusCallback;
import com.george5613.test.helper.ElementHelper;
import com.george5613.test.helper.UiSelectorHelper;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class MainAction extends BaseAction {

    private static final String TAG = Constant.TAOPP.MAIN_ACTIVITY;

    public MainAction(Context context) {
        super(context);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public void doStep() {
        AndroidDriver<AndroidElement> driver = getDriver();
        AndroidElement tabContainer = ElementHelper.findElementById(mContext, "com.taobao.movie.android:id/home_tab_container");
        if (tabContainer == null) return;
        AndroidElement movie = (AndroidElement) tabContainer.findElementByAndroidUIAutomator(UiSelectorHelper.getSelectKeyByText("电影"));
        if (movie == null) return;
        movie.click();
        ElementHelper.waitForElementById(mContext, "com.taobao.movie.android:id/search_view_hint", new ElementStatusCallback() {
            @Override
            public void onElementTouch(AndroidElement element) {
                element.click();
            }
        });
    }
}
