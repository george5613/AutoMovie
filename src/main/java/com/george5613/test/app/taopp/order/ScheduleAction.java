package com.george5613.test.app.taopp.order;

import com.george5613.test.app.global.Context;
import com.george5613.test.base.BaseAction;
import com.george5613.test.callback.ElementStatusCallback;
import com.george5613.test.helper.ElementHelper;
import com.george5613.test.helper.UiSelectorHelper;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import static com.george5613.test.app.global.Constant.TAOPP.SCHEDULE_ACTIVITY;

public class ScheduleAction extends BaseAction {

    private static final String TAG = SCHEDULE_ACTIVITY;
    private static final String TIME = Context.getConfig().getTime();

    private DateFormat mDateFormat;
    private Date mTargetDate;

    public ScheduleAction(Context context) {
        super(context);
        mDateFormat = new SimpleDateFormat("HH:mm");
        try {
            mTargetDate = mDateFormat.parse(TIME);
        } catch (ParseException e) {
            mTargetDate = new Date();
        }
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public void doStep() {
        filterTime();
    }

    private void filterTime() {
        ElementHelper.waitForElementByClass(mContext, "android.support.v7.widget.RecyclerView", new ElementStatusCallback() {
            @Override
            public void onElementTouch(AndroidElement element) {
                int millis = 500;
                Duration duration = Duration.ofMillis(500);
                Dimension dimension = element.getSize();
                MobileElement target;
                MobileElement end;
                do {
                    Point point = element.getLocation();
                    List<MobileElement> elements = element.findElementsByClassName("android.widget.RelativeLayout");
                    target = checkDuration(elements);
                    try {
                        end = element.findElementByAndroidUIAutomator(UiSelectorHelper.getSelectKeyByTextContains("小食和商品"));
                    } catch (NoSuchElementException e) {
                        end = null;
                    }
                    if (target == null) {
                        AndroidTouchAction action = new AndroidTouchAction(getDriver());
                        action.press(PointOption.point(dimension.width / 2, point.getY() + dimension.getHeight() - 1))
                                .waitAction(WaitOptions.waitOptions(duration))
                                .moveTo(PointOption.point(dimension.width / 2, (point.getY() - 1)))
                                .release()
                                .perform();
                    }
                } while (target == null && end == null);
                if (target != null) {
                    target.click();
                }
            }
        });
    }

    private MobileElement checkDuration(List<MobileElement> elements) {
        MobileElement result = null;
        for (MobileElement element : elements) {
            MobileElement start;
            MobileElement end;
            start = ElementHelper.findElementById(element, "com.taobao.movie.android:id/begin_time");
            if (start == null) {
                continue;
            }
            String startTime = start.getText();
            Date startDate = null;
            try {
                startDate = mDateFormat.parse(startTime);
            } catch (ParseException e) {
                break;
            }
            if (mTargetDate.before(startDate)) {
                result = element;
                break;
            }
        }
        return result;
    }
}
