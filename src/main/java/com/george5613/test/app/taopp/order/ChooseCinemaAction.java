package com.george5613.test.app.taopp.order;

import com.george5613.test.app.global.Context;
import com.george5613.test.base.BaseAction;
import com.george5613.test.callback.ElementStatusCallback;
import com.george5613.test.helper.ElementHelper;
import com.george5613.test.helper.UiSelectorHelper;
import com.george5613.test.helper.WaiterHelper;
import io.appium.java_client.android.AndroidElement;

import static com.george5613.test.app.global.Constant.TAOPP.CINEMA_ACTIVITY;

public class ChooseCinemaAction extends BaseAction {

    private static final String TAG = CINEMA_ACTIVITY;

    private static final String DATE = Context.getConfig().getDate();
    private static final String CINEMA_FILTER_KEY = Context.getConfig().getCinemaFilter();
    private static final String CINEMA_NAME = Context.getConfig().getCinemaName();

    public ChooseCinemaAction(Context context) {
        super(context);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public void doStep() {
        filterDate();
        filterIMax();
        WaiterHelper.wait(mContext, 500);
        selectCinema();
    }

    private void filterDate() {
        ElementHelper.waitForElementById(mContext, "com.taobao.movie.android:id/time_filter", new ElementStatusCallback() {
            @Override
            public void onElementTouch(AndroidElement element) {
                AndroidElement tab = (AndroidElement) element.findElementByAndroidUIAutomator(UiSelectorHelper.getSelectKeyByTextContains(DATE));
                if (tab == null) return;
                tab.click();
            }
        });
    }

    private void selectCinema() {
        AndroidElement element = ElementHelper.findElementById(mContext, "com.taobao.movie.android:id/combolist");
        if (element == null) return;
        AndroidElement item = (AndroidElement) element.findElementByAndroidUIAutomator(UiSelectorHelper.getSelectKeyByTextContains(CINEMA_NAME));
        if (item == null) return;
        item.click();
    }

    private void filterIMax() {
        AndroidElement element = ElementHelper.findElementById(mContext, "com.taobao.movie.android:id/filter_all");
        element.click();
        ElementHelper.waitForElementById(mContext, "com.taobao.movie.android:id/cinema_type_recycle", new ElementStatusCallback() {
            @Override
            public void onElementTouch(AndroidElement element) {
                AndroidElement filter = (AndroidElement) element.findElementByAndroidUIAutomator(
                        UiSelectorHelper.getSelectKeyByText(CINEMA_FILTER_KEY));
                if (filter != null) filter.click();
                AndroidElement confirm = getDriver().findElementById("com.taobao.movie.android:id/confirm_btn");
                if (confirm != null) confirm.click();
            }
        });
    }
}
