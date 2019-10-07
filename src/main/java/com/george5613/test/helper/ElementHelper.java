package com.george5613.test.helper;

import com.george5613.test.app.global.Context;
import com.george5613.test.callback.ActivityStatusCallback;
import com.george5613.test.callback.ElementStatusCallback;
import io.appium.java_client.AppiumFluentWait;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.Wait;

import java.time.Clock;
import java.util.List;
import java.util.function.Function;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;

public class ElementHelper {

    private static class ElementStore {

        private AndroidElement mElement;

        public ElementStore() {
        }

        public AndroidElement getElement() {
            return mElement;
        }

        public void storeElement(AndroidElement element) {
            mElement = element;
        }

        public boolean isTouch() {
            return mElement != null;
        }
    }

    public static void waitForElementById(Context context, String rid, ElementStatusCallback callback) {
        ElementStore store = new ElementStore();
        final Wait<ElementStore> wait = new AppiumFluentWait<>(store, Clock.systemDefaultZone(), duration -> {
            store.storeElement(context.getDriver().findElementById(rid));
            Thread.sleep(duration.toMillis());
        }).withPollingStrategy(AppiumFluentWait.IterationInfo::getInterval)
                .withTimeout(ofSeconds(3))
                .pollingEvery(ofMillis(200));
        try {
            wait.until(new Function<ElementStore, Boolean>() {
                @Override
                public Boolean apply(ElementStore elementStore) {
                    return elementStore.isTouch();
                }
            });
            if (callback != null) callback.onElementTouch(store.getElement());
        } catch (TimeoutException ex) {
            if (callback != null) callback.onTimeout();
        }
    }

    public static void waitForElementByClass(Context context, String className, ElementStatusCallback callback) {
        ElementStore store = new ElementStore();
        final Wait<ElementStore> wait = new AppiumFluentWait<>(store, Clock.systemDefaultZone(), duration -> {
            store.storeElement(context.getDriver().findElementByClassName(className));
            Thread.sleep(duration.toMillis());
        }).withPollingStrategy(AppiumFluentWait.IterationInfo::getInterval)
                .withTimeout(ofSeconds(3))
                .pollingEvery(ofMillis(200));
        try {
            wait.until(new Function<ElementStore, Boolean>() {
                @Override
                public Boolean apply(ElementStore elementStore) {
                    return elementStore.isTouch();
                }
            });
            if (callback != null) callback.onElementTouch(store.getElement());
        } catch (TimeoutException ex) {
            if (callback != null) callback.onTimeout();
        }
    }

    public static AndroidElement findElementById(Context context, String rid) {
        AndroidElement result;
        try {
            result = context.getDriver().findElementById(rid);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public static MobileElement findElementById(MobileElement element, String rid) {
        MobileElement result;
        try {
            result = element.findElementById(rid);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public static List<MobileElement> findElementsByClassName(MobileElement element, String className) {
        List<MobileElement> result;
        try {
            result = element.findElementsByClassName(className);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }


}
