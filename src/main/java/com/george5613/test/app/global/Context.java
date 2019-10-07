package com.george5613.test.app.global;

import com.george5613.test.app.SplashAction;
import com.george5613.test.app.WaitingAction;
import com.george5613.test.app.common.PermissionGrantAction;
import com.george5613.test.app.config.MovieConfig;
import com.george5613.test.base.BaseAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

public class Context {

    private static final ArrayList<Class<? extends BaseAction>> ACTIONS = new ArrayList<>();

    private HashMap<String, BaseAction> STEP_ACTIONS = new HashMap<>();
    private String mCurrentActivity = "";
    private long STEP = 500;
    private boolean mFinished = false;
    private AndroidDriver<AndroidElement> mDriver;
    private static MovieConfig sConfig;

    static {
        registerAction(WaitingAction.class);
    }

    public static final void registerAction(Class<? extends BaseAction> action) {
        ACTIONS.add(action);
    }

    public Context(AndroidDriver<AndroidElement> driver, MovieConfig config) {
        this.mDriver = driver;
        this.sConfig = config;
        init();
    }

    private void init() {
        STEP_ACTIONS.clear();
        if (ACTIONS != null && !ACTIONS.isEmpty()) {
            for (Class<? extends BaseAction> clazz : ACTIONS) {
                try {
                    Constructor constructor = clazz.getConstructor(Context.class);
                    if (constructor != null) {
                        BaseAction action = (BaseAction) constructor.newInstance(this);
                        if (action != null) {
                            STEP_ACTIONS.put(action.getTag(), action);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public AndroidDriver<AndroidElement> getDriver() {
        return mDriver;
    }

    public static MovieConfig getConfig() {
        return sConfig;
    }

    public void doStep() {
        BaseAction action = STEP_ACTIONS.get(mCurrentActivity);
        if (action != null) action.doStep();
    }

    public long getNextStep() {
        return STEP;
    }

    public void setNextStep(long step) {
        STEP = step;
    }

    public void printCurrent() {
        System.out.println(mCurrentActivity);
    }

    public void updateCurrentActivity() {
        mCurrentActivity = mDriver.currentActivity();
    }

    public boolean isFinished() {
        return mFinished;
    }

    public void finish() {
        mFinished = true;
    }


}
