package com.george5613.test.app.taopp.order;

import com.george5613.test.app.global.Constant;
import com.george5613.test.app.global.Context;
import com.george5613.test.base.BaseAction;
import com.george5613.test.callback.ElementStatusCallback;
import com.george5613.test.helper.ElementHelper;
import com.george5613.test.helper.MessageHelper;
import com.george5613.test.helper.SmsHelper;
import io.appium.java_client.android.AndroidElement;

public class OrderingAction extends BaseAction {

    private static final String TAG = Constant.TAOPP.ORDERING_ACTIVITY;

    private static final String MOVIE_NAME = Context.getConfig().getMovieName();
    private static final String PHONE_NUM = Context.getConfig().getPhoneNumber();

    public OrderingAction(Context context) {
        super(context);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public void doStep() {
        ElementHelper.waitForElementById(mContext, "com.taobao.movie.android:id/order_bottom_panel_btn_title", new ElementStatusCallback() {
            @Override
            public void onElementTouch(AndroidElement element) {
                SmsHelper.sendSms(getDriver(), PHONE_NUM, MessageHelper.getSmsMsg(MOVIE_NAME));
                mContext.finish();
            }
        });
    }
}
