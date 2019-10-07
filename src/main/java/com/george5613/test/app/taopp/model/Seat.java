package com.george5613.test.app.taopp.model;

import com.george5613.test.app.global.Context;
import com.george5613.test.helper.UiSelectorHelper;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;

public class Seat {

    private Context mContext;
    private AndroidElement mSeatContainer;
    private int mRow;
    private int mColumn;
    private boolean mEnabled = true;
    private boolean isPriceItemExist = false;

    public Seat(String seatNum) {
        mEnabled = false;
        seatNum = seatNum.replace("座", "");
        String[] spite = seatNum.split("排");
        mRow = Integer.parseInt(spite[0]);
        mColumn = Integer.parseInt(spite[1]);
    }

    public Seat(Context context, AndroidElement seatContainer, String seatNum) {
        mContext = context;
        mSeatContainer = seatContainer;
        seatNum = seatNum.replace("座", "");
        String[] spite = seatNum.split("排");
        mRow = Integer.parseInt(spite[0]);
        mColumn = Integer.parseInt(spite[1]);
    }

    public Seat(Context context, AndroidElement seatContainer, int row, int column) {
        mContext = context;
        mSeatContainer = seatContainer;
        this.mRow = row;
        this.mColumn = column;
    }

    public int getRow() {
        return mRow;
    }

    public void setRow(int row) {
        this.mRow = row;
    }

    public int getColumn() {
        return mColumn;
    }

    public void setColumn(int column) {
        this.mColumn = column;
    }

    public boolean isInnerEnabled(){
        return mEnabled;
    }

    public boolean isEnabled() {
        if (!mEnabled) return false;
        if (isPriceItemExist) return true;
        MobileElement targetSeat = mSeatContainer.findElementByAndroidUIAutomator(UiSelectorHelper.getSelectKeyByDescContains(getTag()));
        if (targetSeat != null) {
            targetSeat.click();
        }
        AndroidElement priceContainer = mContext.getDriver().findElementById("com.taobao.movie.android:id/price_container_view");
        MobileElement priceItem = priceContainer.findElementByAndroidUIAutomator(UiSelectorHelper.getSelectKeyByTextContains(getTag()));
        isPriceItemExist = true;
        return priceItem != null;
    }

    public void setEnable(boolean enabled) {
        mEnabled = enabled;
        if (!mEnabled) {
            isPriceItemExist = false;
        }
    }

    public String getTag() {
        return "" + mRow + "排" + mColumn + "座";
    }

    @Override
    public String toString() {
        return getTag();
    }
}
