package com.george5613.test.helper;

import com.george5613.test.callback.ActivityStatusCallback;
import io.appium.java_client.AppiumFluentWait;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.Wait;

import java.time.Clock;
import java.util.function.Function;

import static java.time.Duration.ofSeconds;

public class ActivityHelper {

    private static class ActivityNameStore {

        private String mTouchName;
        private String mActivityName = "";

        public ActivityNameStore(String name) {
            this.mTouchName = name;
        }

        public void storeActivity(String activity) {
            mActivityName = activity;
        }

        public boolean isTouch() {
            return mActivityName.equals(mTouchName);
        }
    }

    public static void waitFor(AndroidDriver<AndroidElement> driver, String activityName, ActivityStatusCallback callback) {
        ActivityNameStore store = new ActivityNameStore(activityName);
        final Wait<ActivityNameStore> wait = new AppiumFluentWait<>(store, Clock.systemDefaultZone(), duration -> {
            store.storeActivity(driver.currentActivity());
            Thread.sleep(duration.toMillis());
        }).withPollingStrategy(AppiumFluentWait.IterationInfo::getInterval)
                .withTimeout(ofSeconds(3))
                .pollingEvery(ofSeconds(1));
        try {
            wait.until(new Function<ActivityNameStore, Boolean>() {
                @Override
                public Boolean apply(ActivityNameStore activityStatus) {
                    return activityStatus.isTouch();
                }
            });
            if (callback != null) callback.onActivityTouch();
        } catch (TimeoutException ex) {
            if (callback != null) callback.onTimeout();
        }
    }

}
