package com.george5613.test.app.taopp.search;

import com.george5613.test.app.global.Constant;
import com.george5613.test.app.global.Context;
import com.george5613.test.base.BaseAction;
import com.george5613.test.callback.ElementStatusCallback;
import com.george5613.test.helper.ElementHelper;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

public class SearchAction extends BaseAction {

    private static final String TAG = Constant.TAOPP.SEARCH_ACTIVITY;
    private static final String MOVIE_NAME = Context.getConfig().getMovieName();

    public SearchAction(Context context) {
        super(context);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public void doStep() {
        AndroidElement element = getDriver().findElementById("com.taobao.movie.android:id/common_search_edit_text");
        if (element == null) return;
        element.sendKeys(MOVIE_NAME);
        getDriver().pressKey(new KeyEvent(AndroidKey.ENTER));
        ElementHelper.waitForElementByClass(mContext, "android.support.v7.widget.RecyclerView", new ElementStatusCallback() {
            @Override
            public void onElementTouch(AndroidElement element) {
                AndroidElement buy = (AndroidElement) element.findElementById("com.taobao.movie.android:id/btn_buy");
                if (buy != null) {
                    buy.click();
                }
            }
        });
    }
}
