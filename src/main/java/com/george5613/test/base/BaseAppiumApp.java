/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.george5613.test.base;

import com.george5613.test.app.global.Constant;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.RELAXED_SECURITY;

public abstract class BaseAppiumApp {

    public static final String APP_ID = "io.appium.android.apis";
    private AppiumDriverLocalService mService;
    protected AndroidDriver<AndroidElement> mDriver;

    /**
     * initialization.
     */
    public void init() {
        initProperties();
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withArgument(RELAXED_SECURITY);
        mService = AppiumDriverLocalService.buildService(builder);
        mService.start();

        if (mService == null || !mService.isRunning()) {
            throw new AppiumServerHasNotBeenStartedLocallyException(
                    "An appium server node is not started!");
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);//设备的系统
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, getDeviceName());//设备名称
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);

        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, Constant.TAOPP.ACTIVITY_PACKAGE);//需要启动的包名
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, Constant.TAOPP.SPLASH_ACTIVITY);//需要启动的Activity
        capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        capabilities.setCapability(AndroidMobileCapabilityType.RESET_KEYBOARD, true);
//        capabilities.setCapability("chromedriverChromeMappingFile", "/Documents/ChromeDriver/version.json");
//        capabilities.setCapability("chromedriverExecutableDir", "/Documents/ChromeDriver");
        mDriver = new AndroidDriver<>(mService.getUrl(), capabilities);
    }

    protected abstract void initProperties();

    protected abstract String getDeviceName();

    protected abstract void start();

    public void quit() {
        if (mDriver != null) {
            mDriver.quit();
        }
        if (mService != null) {
            mService.stop();
        }
    }
}