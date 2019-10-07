package com.george5613.test.helper;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import java.util.Map;

/**
 * 控制adb发送短信
 */
public class SmsHelper {

    public static void sendSms(AndroidDriver<AndroidElement> driver, String phoneNum, String message) {
        StringBuilder builder = new StringBuilder();
        builder.append("am start -a android.intent.action.SENDTO -d sms:")
                .append(phoneNum)
                .append(" --es sms_body \"")
                .append(message)
                .append("\"");
        Map<String, Object> ADB = ImmutableMap.of("command", builder.toString(), "args", "");
        String lsOutput = (String) driver.executeScript("mobile: shell", ADB);
    }
}
