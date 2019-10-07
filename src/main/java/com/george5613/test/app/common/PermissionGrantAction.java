package com.george5613.test.app.common;

import com.george5613.test.app.global.Context;
import com.george5613.test.base.BaseAction;
import com.george5613.test.helper.ElementHelper;
import com.george5613.test.status.ElementStatus;
import io.appium.java_client.AppiumFluentWait;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.ui.Wait;

import java.time.Clock;
import java.util.function.Function;

import static com.george5613.test.app.global.Constant.PERMISSION_ACTIVITY;
import static java.time.Duration.ofSeconds;

public class PermissionGrantAction extends BaseAction {

    public static final String TAG = PERMISSION_ACTIVITY;

    public PermissionGrantAction(Context context) {
        super(context);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public void doStep() {
        grantPermission();
    }

    public void grantPermission() {
        ElementStatus element = new ElementStatus();
        final Wait<ElementStatus> wait = new AppiumFluentWait<>(element, Clock.systemDefaultZone(), duration -> {
            element.setElement(ElementHelper.findElementById(mContext, "com.android.packageinstaller:id/permission_allow_button"));
            Thread.sleep(duration.toMillis());
        }).withPollingStrategy(AppiumFluentWait.IterationInfo::getInterval)
                .withTimeout(ofSeconds(2))
                .pollingEvery(ofSeconds(1));
        try {
            wait.until(new Function<ElementStatus, Boolean>() {
                @Override
                public Boolean apply(ElementStatus elementStatus) {
                    return elementStatus.isFind();
                }
            });
            element.grant();
            grantPermission();
        } catch (Exception ex) {
            return;
        }
    }
}
