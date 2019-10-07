package com.george5613.test.helper;

import com.george5613.test.app.global.Context;

import java.util.concurrent.TimeUnit;

public class WaiterHelper {

    public static void wait(Context context, long millis) {
        context.getDriver().manage().timeouts().implicitlyWait(millis, TimeUnit.MILLISECONDS);
    }

}
